package net.jlxxw.robot.filter.servlet.filter;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.core.identity.ClientIdentification;
import net.jlxxw.robot.filter.servlet.template.AbstractFilterTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * client-id identifier
 * @author chunyang.leng
 * @date 2022-11-03 3:45 PM
 */
public class RobotIdentificationFilter extends AbstractFilterTemplate {
    private static final String CLIENT_ID = "x-trace-client-id";
    @Autowired
    private ClientIdentification clientIdentification;

    public RobotIdentificationFilter(FilterProperties filterProperties) {
        super(filterProperties);
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
    @Override
    protected void filter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException, RuleException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (Objects.isNull(cookies)) {
            createClientId(httpServletRequest,(HttpServletResponse) response);
            chain.doFilter(request,response);
            return;
        }
        boolean hasCookies = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CLIENT_ID)){
                hasCookies = true;
                break;
            }
        }
        if (!hasCookies){
            createClientId(httpServletRequest,(HttpServletResponse) response);
            chain.doFilter(request,response);
        }

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
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
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
    @Override
    public void destroy() {
        super.destroy();
    }


    private String createClientId(HttpServletRequest request, HttpServletResponse response){
        try {
            String host = request.getHeader("Host");
            String id = clientIdentification.createClientId();
            Cookie cookie = new Cookie(CLIENT_ID,id);
            cookie.setDomain(host);
            cookie.setPath("/");
            // 7 day
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
