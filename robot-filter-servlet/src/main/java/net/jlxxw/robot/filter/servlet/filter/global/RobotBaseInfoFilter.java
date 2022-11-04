package net.jlxxw.robot.filter.servlet.filter.global;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.core.identity.ClientIdentification;
import net.jlxxw.robot.filter.servlet.context.RobotServletFilterWebContext;
import net.jlxxw.robot.filter.servlet.template.AbstractFilterTemplate;
import net.jlxxw.robot.filter.servlet.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * get base info and save to RobotServletFilterWebContext
 *
 * @author chunyang.leng
 * @date 2022-11-04 10:02 AM
 * @see RobotServletFilterWebContext
 */
@Component
public class RobotBaseInfoFilter extends AbstractFilterTemplate {
    private static final Logger logger = LoggerFactory.getLogger(RobotBaseInfoFilter.class);
    @Autowired
    private IpUtils ipUtils;
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private RobotFilterProperties robotFilterProperties;
    @Autowired
    private ClientIdentification clientIdentification;

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
        FilterChain chain, FilterProperties filterProperties) throws IOException, ServletException, RuleException {

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String ipAddress = ipUtils.getIpAddress(httpServletRequest);
            String origin = httpServletRequest.getHeader("Origin");
            String referer = httpServletRequest.getHeader("Referer");
            String host = httpServletRequest.getHeader("Host");
            String authorization = httpServletRequest.getHeader("Authorization");
            RobotServletFilterWebContext.setIp(ipAddress);
            RobotServletFilterWebContext.setHost(host);
            RobotServletFilterWebContext.setAuthorization(authorization);
            RobotServletFilterWebContext.setReferer(referer);
            RobotServletFilterWebContext.setOrigin(origin);

            Cookie[] cookies = httpServletRequest.getCookies();
            if (Objects.isNull(cookies)) {
                String clientId = createClientId( (HttpServletResponse) response);
                RobotServletFilterWebContext.setClientId(clientId);
                return;
            }
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(robotFilterProperties.getTrace().getName())){
                    RobotServletFilterWebContext.setClientId(cookie.getValue());
                    return;
                }
            }
            String clientId = createClientId( (HttpServletResponse) response);
            RobotServletFilterWebContext.setClientId(clientId);

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

    /**
     * count the current IP qps
     *
     * @param ip client ip
     * @return qps
     */
    @Override protected Integer getQpsByIp(String ip) {
        return null;
    }

    /**
     * count the current client id qps
     *
     * @param clientId client id
     * @return qps
     */
    @Override protected Integer getQpsByClientId(String clientId) {
        return null;
    }

    private String createClientId( HttpServletResponse response){
        try {
            String id = clientIdentification.createClientId();
            Cookie cookie = new Cookie(robotFilterProperties.getTrace().getName(),id);
            cookie.setDomain(RobotServletFilterWebContext.getHost());
            cookie.setPath("/");
            cookie.setMaxAge(robotFilterProperties.getTrace().getMaxAge());
            response.addCookie(cookie);
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
