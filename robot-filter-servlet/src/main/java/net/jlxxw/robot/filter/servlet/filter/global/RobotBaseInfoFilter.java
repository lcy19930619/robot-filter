package net.jlxxw.robot.filter.servlet.filter.global;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.trace.RobotFilterTraceProperties;
import net.jlxxw.robot.filter.core.identity.ClientIdentification;
import net.jlxxw.robot.filter.servlet.context.RobotServletFilterWebContext;
import net.jlxxw.robot.filter.servlet.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * get base info and save to RobotServletFilterWebContext
 *
 * @author chunyang.leng
 * @date 2022-11-04 10:02 AM
 * @see RobotServletFilterWebContext
 */
@Order(Integer.MIN_VALUE + 4)
@Component
public class RobotBaseInfoFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RobotBaseInfoFilter.class);
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private IpUtils ipUtils;
    @Autowired
    private RobotFilterTraceProperties robotFilterTraceProperties;
    @Autowired
    private ClientIdentification clientIdentification;

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
        logUtils.debug(logger, "data arrival filter: RobotBaseInfoFilter");

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String ipAddress = ipUtils.getIpAddress(httpServletRequest);
            String origin = httpServletRequest.getHeader("Origin");
            String referer = httpServletRequest.getHeader("Referer");
            String host = httpServletRequest.getHeader("Host");
            String authorization = httpServletRequest.getHeader("Authorization");
            RobotServletFilterWebContext.setIp(ipAddress);
            RobotServletFilterWebContext.setHost(host.split(":")[0]);
            RobotServletFilterWebContext.setAuthorization(authorization);
            RobotServletFilterWebContext.setReferer(referer);
            RobotServletFilterWebContext.setOrigin(origin);

            Cookie[] cookies = httpServletRequest.getCookies();
            if (Objects.isNull(cookies)) {
                String clientId = createClientId((HttpServletResponse) response);
                RobotServletFilterWebContext.setClientId(clientId);
                logUtils.info(logger, "ip:{} no has cookie, creation client id: {}", ipAddress, clientId);
                return;
            }
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(robotFilterTraceProperties.getName())) {
                    RobotServletFilterWebContext.setClientId(cookie.getValue());
                    return;
                }
            }
            String clientId = createClientId((HttpServletResponse) response);
            RobotServletFilterWebContext.setClientId(clientId);
            logUtils.info(logger, "ip:{} creation client id: {}", ipAddress, clientId);

            logUtils.debug(logger, "robot filter get base info success,client ip: {},origin:{},referer:{},host:{}", ipAddress, origin, referer, host);
        } catch (Exception e) {
            logUtils.error(logger, "robot filter get base info failed!!!", e);
        } finally {
            try {
                chain.doFilter(request, response);
            } finally {
                RobotServletFilterWebContext.remove();
            }
        }
    }

    private String createClientId(HttpServletResponse response) {
        try {
            String ip = RobotServletFilterWebContext.getIp();
            String id = clientIdentification.createClientId(ip);
            Cookie cookie = new Cookie(robotFilterTraceProperties.getName(), id);
            cookie.setDomain(RobotServletFilterWebContext.getHost());
            cookie.setPath("/");
            cookie.setMaxAge(robotFilterTraceProperties.getMaxAge());
            response.addCookie(cookie);
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
