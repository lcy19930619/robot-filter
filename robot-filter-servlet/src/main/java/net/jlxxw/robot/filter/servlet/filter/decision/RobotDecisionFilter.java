package net.jlxxw.robot.filter.servlet.filter.decision;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jlxxw.robot.filter.common.event.AddClientIdToBlackListEvent;
import net.jlxxw.robot.filter.common.event.AddIpToBlackListEvent;
import net.jlxxw.robot.filter.common.event.ReceiveRequestEvent;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.filter.FilterProperties;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.core.data.DataCore;
import net.jlxxw.robot.filter.servlet.context.RobotServletFilterWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * if request form robot,this filter decision reject
 *
 * @author chunyang.leng
 * @date 2022-11-04 11:32 AM
 */
public class RobotDecisionFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RobotDecisionFilter.class);
    private FilterProperties filterProperties;
    private ApplicationContext applicationContext;
    private LogUtils logUtils;
    private RuleProperties ruleProperties;
    private DataCore dataCore;

    private String filterName;

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
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logUtils.info(logger, "filter:{} initialized", filterName);
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
        if (!filterProperties.isEnabled()) {
            chain.doFilter(request, response);
            return;
        }
        logUtils.debug(logger, "data arrival filter: {}", filterName);
        boolean inWhiteList = RobotServletFilterWebContext.inWhiteList();
        if (inWhiteList) {
            chain.doFilter(request, response);
        } else {
            String clientId = RobotServletFilterWebContext.getClientId();
            String host = RobotServletFilterWebContext.getHost();
            String ip = RobotServletFilterWebContext.getIp();

            boolean allow = dataCore.allowClientId(filterProperties.getName(), ruleProperties.getName(), clientId);
            if (!allow) {
                applicationContext.publishEvent(new AddClientIdToBlackListEvent( clientId, filterProperties.getName(), ruleProperties));
                throw new RuleException("reject request, max allow !!!", ruleProperties);
            }
            allow = dataCore.allowIp(filterProperties.getName(), ruleProperties.getName(), ip);
            if (!allow) {
                applicationContext.publishEvent(new AddIpToBlackListEvent(ip, filterProperties.getName(), ruleProperties));
                throw new RuleException("reject request, max allow !!!", ruleProperties);
            }

            ReceiveRequestEvent event = new ReceiveRequestEvent(ip, host, clientId, filterProperties.getName(), ruleProperties.getName());
            applicationContext.publishEvent(event);
        }
        chain.doFilter(request, response);

    }

    public void setFilterProperties(FilterProperties filterProperties) {
        this.filterProperties = filterProperties;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setLogUtils(LogUtils logUtils) {
        this.logUtils = logUtils;
    }

    public void setRuleProperties(RuleProperties ruleProperties) {
        this.ruleProperties = ruleProperties;
    }

    public void setDataCore(DataCore dataCore) {
        this.dataCore = dataCore;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
