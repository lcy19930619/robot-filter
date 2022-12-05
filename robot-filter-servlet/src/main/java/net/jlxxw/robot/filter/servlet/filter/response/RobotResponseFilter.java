package net.jlxxw.robot.filter.servlet.filter.response;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.servlet.filter.global.RobotGlobalAuthorizationWhiteFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.NestedServletException;

/**
 * handler RuleException
 *
 * @author chunyang.leng
 * @date 2022-11-03 12:45 PM
 */
@Order(Integer.MIN_VALUE)
@Component
public class RobotResponseFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RobotGlobalAuthorizationWhiteFilter.class);
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private RobotFilterProperties robotFilterProperties;

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
        logUtils.info(logger,"filter:RobotResponseFilter initialized");
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
     * The <code>doFilter</code> method of the Filter is called by the container
     * each time a request/response pair is passed through the chain due to a
     * client request for a resource at the end of the chain. The FilterChain
     * passed in to this method allows the Filter to pass on the request and
     * response to the next entity in the chain.
     * <p>
     * A typical implementation of this method would follow the following
     * pattern:- <br>
     * 1. Examine the request<br>
     * 2. Optionally wrap the request object with a custom implementation to
     * filter content or headers for input filtering <br>
     * 3. Optionally wrap the response object with a custom implementation to
     * filter content or headers for output filtering <br>
     * 4. a) <strong>Either</strong> invoke the next entity in the chain using
     * the FilterChain object (<code>chain.doFilter()</code>), <br>
     * 4. b) <strong>or</strong> not pass on the request/response pair to the
     * next entity in the filter chain to block the request processing<br>
     * 5. Directly set headers on the response after invocation of the next
     * entity in the filter chain.
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this
     *                 filter to pass the request and response to for further
     *                 processing
     * @throws IOException      if an I/O error occurs during this filter's
     *                          processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        try {
            logUtils.debug(logger, "data arrival filter: RobotResponseFilter");
            chain.doFilter(request, response);
        } catch (RuleException e) {
           handles(e,response);
        }catch (NestedServletException e){
            Throwable cause = e.getCause();
            if (cause instanceof RuleException){
                handles((RuleException) cause, response);
                return;
            }
            throw e;
        }
    }

    private void handles(RuleException e,ServletResponse response) throws IOException {
        RuleProperties properties = e.getRuleProperties();
        boolean returnRejectMessage = properties.isReturnRejectMessage();
        int httpCode = properties.getHttpResponseCode();

        String contentType = properties.getContentType();

        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.setContentType(contentType);
        servletResponse.setStatus(httpCode);
        if (returnRejectMessage) {
            String message = e.getMessage();
            if (StringUtils.isNotBlank(properties.getRejectMessage())){
                message = properties.getRejectMessage();
            }
            servletResponse.getWriter().println(message);
        }
        logUtils.info(logger, "RobotResponseFilter handler rejected message:{}",e.getMessage());

    }
}
