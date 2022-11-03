package net.jlxxw.robot.filter.servlet.template;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;

/**
 * @author chunyang.leng
 * @date 2022-11-03 9:41 AM
 */
public abstract class AbstractFilterTemplate implements Filter {

    /**
     * filter properties
     */
    private final FilterProperties filterProperties;

    public AbstractFilterTemplate(FilterProperties filterProperties) {
        this.filterProperties = filterProperties;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        boolean enable = filterProperties.isEnable();
        if (enable) {
            filter(request,response);
        }else {
            chain.doFilter(request,response);
        }
    }

    /**
     * filter function
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @throws RuleException Robot limit triggered
     */
    protected abstract void filter(ServletRequest request, ServletResponse response)throws IOException, ServletException, RuleException;

    public FilterProperties getFilterProperties() {
        return filterProperties;
    }

}
