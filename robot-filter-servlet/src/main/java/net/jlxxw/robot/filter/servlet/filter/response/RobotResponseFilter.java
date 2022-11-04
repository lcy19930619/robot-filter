package net.jlxxw.robot.filter.servlet.filter.response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.Integer;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.servlet.template.AbstractFilterTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * handler RuleException
 * @author chunyang.leng
 * @date 2022-11-03 12:45 PM
 */
@Component
public class RobotResponseFilter extends AbstractFilterTemplate {

    @Autowired
    private RobotFilterProperties robotFilterProperties;

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
        FilterChain chain, FilterProperties filterProperties) throws IOException, ServletException, RuleException {
        try {
            chain.doFilter(request, response);
        } catch (RuleException e) {
            boolean returnRejectMessage = robotFilterProperties.getResponse().isReturnRejectMessage();

            int httpCode = filterProperties.getRule().getHttpResponseCode();

            String contentType = filterProperties.getRule().getContentType();

            HttpServletResponse servletResponse = (HttpServletResponse) response;
            servletResponse.setContentType(contentType);
            servletResponse.setStatus(httpCode);
            if (returnRejectMessage) {
                servletResponse.getWriter().println(e.getMessage());
            }

        }
    }

    /**
     * current filter increase the client id counter once
     *
     * @param clientId client id
     */
    @Override protected void incClientId(String clientId) {

    }

    /**
     * current filter increase the ip counter once
     *
     * @param ip client ip
     */
    @Override protected void incIp(String ip) {

    }

    /**
     * count current filter all client id
     *
     * @return key: client id,value: count
     */
    @Override protected Map<String, Integer> countClientId() {
        return null;
    }

    /**
     * count current filter all ip
     *
     * @return key: ip,value: count
     */
    @Override protected Map<String, Integer> countIp() {
        return null;
    }
}
