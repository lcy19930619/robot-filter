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
    private FilterProperties filterProperties;

    public AbstractFilterTemplate(){}
    public AbstractFilterTemplate(FilterProperties filterProperties) {
        this.filterProperties = filterProperties;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        if (filterProperties.isEnable()) {
            filter(request, response, chain);
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
        FilterChain chain) throws IOException, ServletException, RuleException;


}
