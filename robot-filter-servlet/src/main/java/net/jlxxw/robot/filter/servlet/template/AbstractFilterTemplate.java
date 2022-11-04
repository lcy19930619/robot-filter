package net.jlxxw.robot.filter.servlet.template;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jlxxw.robot.filter.common.event.ReceiveRequestEvent;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.servlet.context.RobotServletFilterWebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author chunyang.leng
 * @date 2022-11-03 9:41 AM
 */
public abstract class AbstractFilterTemplate implements Filter {

    /**
     * filter properties
     */
    private FilterProperties filterProperties;
    @Autowired
    private ApplicationContext applicationContext;

    public AbstractFilterTemplate(){}

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        if (filterProperties.isEnable()) {
            boolean inWhiteList = RobotServletFilterWebContext.inWhiteList();
            if (inWhiteList) {
                chain.doFilter(request, response);
            }else {
                String clientId = RobotServletFilterWebContext.getClientId();
                String host = RobotServletFilterWebContext.getHost();
                String ip = RobotServletFilterWebContext.getIp();
                ReceiveRequestEvent event = new ReceiveRequestEvent(ip,host,clientId,filterProperties.getName());
                applicationContext.publishEvent(event);
                Integer qpsByClientId = getQpsByClientId(clientId);
                Integer qpsByIp = getQpsByIp(ip);

                long maxQps = filterProperties.getRule().getMaxQps();
                if (qpsByClientId != null && qpsByClientId > maxQps){
                    throw new RuleException("QPS must be less than than " + maxQps);
                }
                if (qpsByIp != null && qpsByIp > maxQps){
                    throw new RuleException("QPS must be less than than " + maxQps);
                }

                filter(request, response, chain,filterProperties);

                incIp(ip);
                incClientId(clientId);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
    public FilterProperties getFilterProperties() {
        return filterProperties;
    }

    public void setFilterProperties(FilterProperties filterProperties) {
        this.filterProperties = filterProperties;
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
        Filter.super.init(filterConfig);
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
        Filter.super.destroy();
    }

    /**
     * filter function
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @throws RuleException    Robot limit triggered
     */
    protected abstract void filter(ServletRequest request, ServletResponse response,
        FilterChain chain,FilterProperties filterProperties) throws IOException, ServletException, RuleException;

    /**
     * current filter increase the client id counter once
     * @param clientId client id
     */
    protected abstract void incClientId(String clientId);

    /**
     * current filter increase the ip counter once
     * @param ip client ip
     */
    protected abstract void incIp(String ip);

    /**
     * count current filter all client id
     * @return key: client id,value: qps
     */
    protected abstract Map<String, Integer> countClientId();

    /**
     * count current filter all ip
     * @return key: ip,value: qps
     */
    protected abstract Map<String,Integer> countIp();

    /**
     * count the current IP qps
     * @param ip client ip
     * @return qps
     */
    protected abstract Integer getQpsByIp(String ip);

    /**
     * count the current client id qps
     * @param clientId client id
     * @return qps
     */
    protected abstract Integer getQpsByClientId(String clientId);
}
