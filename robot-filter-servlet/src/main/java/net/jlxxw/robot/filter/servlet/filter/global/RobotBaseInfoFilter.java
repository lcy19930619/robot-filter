package net.jlxxw.robot.filter.servlet.filter.global;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.servlet.context.RobotServletFilterWebContext;
import net.jlxxw.robot.filter.servlet.template.AbstractFilterTemplate;
import net.jlxxw.robot.filter.servlet.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * get base info and save to RobotServletFilterWebContext
 * @see RobotServletFilterWebContext
 * @author chunyang.leng
 * @date 2022-11-04 10:02 AM
 */
public class RobotBaseInfoFilter extends AbstractFilterTemplate {

    @Autowired
    private IpUtils ipUtils;

    public RobotBaseInfoFilter(FilterProperties filterProperties) {
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

    }

}
