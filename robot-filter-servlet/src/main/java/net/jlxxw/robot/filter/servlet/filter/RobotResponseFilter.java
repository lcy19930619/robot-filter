package net.jlxxw.robot.filter.servlet.filter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.config.properties.ResponseProperties;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.servlet.template.AbstractFilterTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2022-11-03 12:45 PM
 */
public class RobotResponseFilter extends AbstractFilterTemplate {
    private static final String RESPONSE_PROPERTIES_KEY = "responseProperties";
    private static final String ENABLED_RESPONSE_REJECT_MESSAGE_KEY = "returnRejectMessage";
    private static final String HTTP_CODE_KEY = "httpCode";
    private static final String CONTENT_TYPE_KEY = "contentType";

    @Autowired
    private RobotFilterProperties robotFilterProperties;



    public RobotResponseFilter(FilterProperties filterProperties) {
        super(filterProperties);
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
    @Override public void destroy() {
        super.destroy();
    }

    /**
     * filter function
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @throws RuleException    Robot limit triggered
     */
    @Override
    protected void filter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException, RuleException {
        try {

            chain.doFilter(request, response);
        } catch (RuleException e) {
            try {
                boolean returnRejectMessage = (boolean)cache.get(ENABLED_RESPONSE_REJECT_MESSAGE_KEY, () -> {
                    ResponseProperties responseProperties = robotFilterProperties.getResponse();
                    return responseProperties.isReturnRejectMessage();
                });

                int httpCode = (int)cache.get(HTTP_CODE_KEY, () -> getFilterProperties().getRule().getHttpResponseCode());

                String contentType = (String)cache.get(HTTP_CODE_KEY, () -> getFilterProperties().getRule().getContentType());

                HttpServletResponse servletResponse = (HttpServletResponse)response;
                servletResponse.setContentType(contentType);
                servletResponse.setStatus(httpCode);
                if (returnRejectMessage){
                    servletResponse.getWriter().println(e.getMessage());
                }
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
