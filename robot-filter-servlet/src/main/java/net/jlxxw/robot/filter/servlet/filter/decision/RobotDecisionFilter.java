package net.jlxxw.robot.filter.servlet.filter.decision;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jlxxw.robot.filter.common.event.ReceiveRequestEvent;
import net.jlxxw.robot.filter.config.properties.filter.FilterProperties;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.core.data.DataCore;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.core.limit.SimpleCountUtils;
import net.jlxxw.robot.filter.core.lru.LimitLru;
import net.jlxxw.robot.filter.core.vo.ClientIdCountVO;
import net.jlxxw.robot.filter.core.vo.IpCountVO;
import net.jlxxw.robot.filter.core.vo.RobotClientIdVO;
import net.jlxxw.robot.filter.core.vo.RobotIpVO;
import net.jlxxw.robot.filter.servlet.context.RobotServletFilterWebContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

/**
 * todo done by lcy 2022/11/05
 * if request form robot,this filter decision reject
 *
 * @author chunyang.leng
 * @date 2022-11-04 11:32 AM
 */
public class RobotDecisionFilter implements Filter, DataCore {

    private FilterProperties filterProperties;
    @Autowired
    private ApplicationContext applicationContext;
    /**
     * key rule name
     * value: key client id,value : count util
     */
    private Map<String, Map<String, SimpleCountUtils>> ruleClientIdLru = null;

    /**
     * key rule name
     * value: key client ip,value count util
     */
    private Map<String, Map<String, SimpleCountUtils>> ruleIpLru = null;

    /**
     * limit
     */
    private int lru = 200;

    @PostConstruct
    private void init() {
        lru = filterProperties.getLru();
        ruleClientIdLru = new LimitLru<>(lru);
        ruleIpLru = new LimitLru<>(lru);

        Set<String> nameSet = new HashSet<>();
        List<RuleProperties> rules = filterProperties.getRules();
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        rules.forEach(x -> {
            if (StringUtils.isBlank(x.getName())) {
                throw new BeanCreationException("filter: " + filterProperties.getName() + ",rule name is null !!!");
            }
            if (nameSet.contains(x.getName())) {
                throw new BeanCreationException("filter: " + filterProperties.getName() + ",rule name '" + x.getName() + "' is repeat !!!");
            }
            nameSet.add(x.getName());
        });
    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * placed into service. The servlet container calls the init method exactly
     * once after instantiating the filter. The init method must complete
     * successfully before the filter is asked to do any filtering work.
     * <p>
     * The web container cannot place the filter into service if the init method
     * either:
     * <ul>
     * <li>Throws a ServletException</li>
     * <li>Does not return within a time period defined by the web
     *     container</li>
     * </ul>
     * The default implementation is a NO-OP.
     *
     * @param filterConfig The configuration information associated with the
     *                     filter instance being initialised
     * @throws ServletException if the initialisation fails
     */
    @Override public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service. This method is only called once all threads within
     * the filter's doFilter method have exited or after a timeout period has
     * passed. After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter. <br>
     * <br>
     * <p>
     * This method gives the filter an opportunity to clean up any resources
     * that are being held (for example, memory, file handles, threads) and make
     * sure that any persistent state is synchronized with the filter's current
     * state in memory.
     * <p>
     * The default implementation is a NO-OP.
     */
    @Override public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        if (!filterProperties.isEnable()) {
            chain.doFilter(request, response);
            return;
        }
        boolean inWhiteList = RobotServletFilterWebContext.inWhiteList();
        if (inWhiteList) {
            chain.doFilter(request, response);
        } else {
            String clientId = RobotServletFilterWebContext.getClientId();
            String host = RobotServletFilterWebContext.getHost();
            String ip = RobotServletFilterWebContext.getIp();


            List<RuleProperties> rules = filterProperties.getRules();
            if (!CollectionUtils.isEmpty(rules)) {

                for (RuleProperties rule : rules) {

                    int maxAllow = rule.getMaxAllow();
                    String name = rule.getName();

                    Map<String, SimpleCountUtils> ipLru = ruleIpLru.get(name);
                    if (Objects.isNull(ipLru)) {
                        ipLru = new LimitLru<>(lru);
                        ruleIpLru.put(name, ipLru);
                    }

                    SimpleCountUtils ipSimpleCountUtils = ipLru.get(ip);
                    if (Objects.isNull(ipSimpleCountUtils)) {
                        ipSimpleCountUtils = new SimpleCountUtils(maxAllow);
                        ipLru.put(ip, ipSimpleCountUtils);
                    }

                    Map<String, SimpleCountUtils> clientIdLru = ruleClientIdLru.get(name);
                    if (Objects.isNull(clientIdLru)) {
                        clientIdLru = new LimitLru<>(lru);
                        ruleClientIdLru.put(name, clientIdLru);
                    }

                    SimpleCountUtils clientIdSimpleCountUtils = clientIdLru.get(ip);
                    if (Objects.isNull(clientIdSimpleCountUtils)) {
                        clientIdSimpleCountUtils = new SimpleCountUtils(maxAllow);
                        ipLru.put(ip, clientIdSimpleCountUtils);
                    }

                    int passByClientId = countCurrentPassByClientId(clientId, name);
                    int passByByIp = countCurrentPassByIp(ip, name);
                    if (passByClientId > maxAllow) {
                        throw new RuleException("maxAllow must be less than than " + maxAllow, rule);
                    }
                    if (passByByIp > maxAllow) {
                        throw new RuleException("maxAllow must be less than than " + maxAllow, rule);
                    }
                    incIp(ip, name);
                    incClientId(clientId, name);
                    ReceiveRequestEvent event = new ReceiveRequestEvent(ip, host, clientId, filterProperties.getName(),name);
                    applicationContext.publishEvent(event);
                }
            }

        }
        chain.doFilter(request, response);

    }

    /**
     * current filter increase the client id counter once
     *
     * @param clientId client id
     * @param ruleName name of the rule
     */
    @Override
    public void incClientId(String clientId, String ruleName) {
        Map<String, SimpleCountUtils> map = ruleClientIdLru.get(ruleName);
        SimpleCountUtils simpleCountUtils = map.get(clientId);
        simpleCountUtils.incrementAndGet();
    }

    /**
     * current filter increase the ip counter once
     *
     * @param ip       client ip
     * @param ruleName rule name
     */
    @Override
    public void incIp(String ip, String ruleName) {
        Map<String, SimpleCountUtils> map = ruleIpLru.get(ruleName);
        SimpleCountUtils simpleCountUtils = map.get(ip);
        simpleCountUtils.incrementAndGet();
    }

    /**
     * count current filter all client id
     *
     * @return key: client id,value: qps
     */
    @Override
    public RobotClientIdVO countClientId() {
        Map<String,Map<String,SimpleCountUtils>> clone = ruleIpLru;
        RobotClientIdVO vo = new RobotClientIdVO();
        vo.setFilterName(filterProperties.getName());
        Map<String,List<ClientIdCountVO>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(clone)){
            vo.setData(map);
            return vo;
        }

        clone.forEach((k,v)->{
            List<ClientIdCountVO> list = new LinkedList<>();
            v.forEach((k1,v1)->{
                ClientIdCountVO data = new ClientIdCountVO();
                data.setClientId(k1);
                data.setCount(v1.currentPass());
                list.add(data);
            });
            map.put(k,list);
        });
        vo.setData(map);
        return vo;
    }

    /**
     * count current filter all ip
     *
     * @return key: ip,value: qps
     */
    @Override
    public RobotIpVO countIp() {
        Map<String,Map<String,SimpleCountUtils>> clone = ruleClientIdLru;
        RobotIpVO vo = new RobotIpVO();
        vo.setFilterName(filterProperties.getName());
        Map<String,List<IpCountVO>> map = new HashMap<String,List<IpCountVO>>();
        if (CollectionUtils.isEmpty(clone)){
            vo.setData(map);
            return vo;
        }

        clone.forEach((k,v)->{
            List<IpCountVO> list = new LinkedList<>();
            v.forEach((k1,v1)->{
                IpCountVO data = new IpCountVO();
                data.setIp(k1);
                data.setCount(v1.currentPass());
                list.add(data);
            });
            map.put(k,list);
        });
        vo.setData(map);

        return vo;
    }

    /**
     * count the current IP qps
     *
     * @param ip       client ip
     * @param ruleName name of the rule
     * @return qps
     */
    @Override
    public int countCurrentPassByIp(String ip, String ruleName) {
        Map<String, SimpleCountUtils> map = ruleIpLru.get(ruleName);
        if (CollectionUtils.isEmpty(map)) {
            return 0;
        }
        SimpleCountUtils simpleCountUtils = map.get(ip);
        if (Objects.isNull(simpleCountUtils)) {
            return 0;
        }
        return simpleCountUtils.currentPass();
    }

    /**
     * count the current client id qps
     *
     * @param clientId client id
     * @param ruleName name of the rule
     * @return qps
     */
    @Override
    public int countCurrentPassByClientId(String clientId, String ruleName) {
        Map<String, SimpleCountUtils> map = ruleClientIdLru.get(ruleName);
        if (CollectionUtils.isEmpty(map)) {
            return 0;
        }
        SimpleCountUtils simpleCountUtils = map.get(clientId);
        if (Objects.isNull(simpleCountUtils)) {
            return 0;
        }
        return simpleCountUtils.currentPass();
    }

    public FilterProperties getFilterProperties() {
        return filterProperties;
    }

    public void setFilterProperties(FilterProperties filterProperties) {
        this.filterProperties = filterProperties;
    }
}
