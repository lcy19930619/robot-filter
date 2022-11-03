package net.jlxxw.robot.filter.servlet.template;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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
    private static final String enabledKey = "enabled";

    /**
     * filter properties
     */
    private final FilterProperties filterProperties;

    protected final Cache cache;

    public AbstractFilterTemplate(FilterProperties filterProperties) {
        this.filterProperties = filterProperties;
        cache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .refreshAfterWrite(10, TimeUnit.SECONDS)
            .build();
    }

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        boolean enable  = false;
        try {
            enable = (boolean)cache.get(enabledKey, filterProperties::isEnable);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (enable) {
            filter(request,response,chain);
        }else {
            chain.doFilter(request,response);
        }
    }

    /**
     * filter function
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @throws RuleException Robot limit triggered
     */
    protected abstract void filter(ServletRequest request, ServletResponse response,FilterChain chain)throws IOException, ServletException, RuleException;

    public FilterProperties getFilterProperties() {
        return filterProperties;
    }

}
