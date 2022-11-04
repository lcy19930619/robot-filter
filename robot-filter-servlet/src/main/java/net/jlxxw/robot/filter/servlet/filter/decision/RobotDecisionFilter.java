package net.jlxxw.robot.filter.servlet.filter.decision;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jlxxw.robot.filter.common.event.ReceiveRequestEvent;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.config.properties.RuleProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.core.limit.SimpleCountUtils;
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
public class RobotDecisionFilter implements Filter {

    private FilterProperties filterProperties;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * key is rule name
     * value is SimpleCountUtils
     */
    private Map<String, SimpleCountUtils> rulePass = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        List<RuleProperties> rules = filterProperties.getRules();
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        rules.forEach(x -> {
            if (StringUtils.isBlank(x.getName())) {
                throw new BeanCreationException("filter: " + filterProperties.getName() + ",rule name is null !!!");
            }
            if (rulePass.containsKey(x.getName())) {
                throw new BeanCreationException("filter: " + filterProperties.getName() + ",rule name '" + x.getName() + "' is repeat !!!");
            }
            rulePass.put(x.getName(), new SimpleCountUtils(x.getInterval()));
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
            ReceiveRequestEvent event = new ReceiveRequestEvent(ip, host, clientId, filterProperties.getName());
            applicationContext.publishEvent(event);

            List<RuleProperties> rules = filterProperties.getRules();
            if (!CollectionUtils.isEmpty(rules)) {

                for (RuleProperties rule : rules) {
                    int maxAllow = rule.getMaxAllow();
                    String name = rule.getName();

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
                }
            }

        }

    }

    /**
     * current filter increase the client id counter once
     *
     * @param clientId client id
     * @param ruleName name of the rule
     */
    protected void incClientId(String clientId, String ruleName) {

    }

    /**
     * current filter increase the ip counter once
     *
     * @param ip       client ip
     * @param ruleName rule name
     */
    protected void incIp(String ip, String ruleName) {

    }

    /**
     * count current filter all client id
     *
     * @return key: client id,value: qps
     */
    protected RobotClientIdVO countClientId() {
        return null;
    }

    /**
     * count current filter all ip
     *
     * @return key: ip,value: qps
     */
    protected RobotIpVO countIp() {
        return null;
    }

    /**
     * count the current IP qps
     *
     * @param ip       client ip
     * @param ruleName name of the rule
     * @return qps
     */
    protected int countCurrentPassByIp(String ip, String ruleName) {
        return 0;
    }

    /**
     * count the current client id qps
     *
     * @param clientId client id
     * @param ruleName name of the rule
     * @return qps
     */
    protected int countCurrentPassByClientId(String clientId, String ruleName) {
        return 0;
    }

    public FilterProperties getFilterProperties() {
        return filterProperties;
    }

    public void setFilterProperties(FilterProperties filterProperties) {
        this.filterProperties = filterProperties;
    }
}
