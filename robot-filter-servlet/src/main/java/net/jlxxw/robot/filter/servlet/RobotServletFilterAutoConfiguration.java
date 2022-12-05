package net.jlxxw.robot.filter.servlet;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.filter.FilterProperties;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.config.properties.filter.servlet.RobotFilterServletFilterProperties;
import net.jlxxw.robot.filter.core.data.DataCore;
import net.jlxxw.robot.filter.servlet.filter.decision.RobotDecisionFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * servlet filter auto configuration
 * @author lcy
 */
@ConditionalOnClass(Servlet.class)
@ComponentScan("net.jlxxw.robot.filter.servlet")
@Configuration
public class RobotServletFilterAutoConfiguration implements  ServletContextInitializer {
    private static final Logger logger = LoggerFactory.getLogger(RobotServletFilterAutoConfiguration.class);
    @Autowired
    private RobotFilterServletFilterProperties robotFilterServletFilterProperties;
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private List<Filter> filters;
    @Autowired
    private DataCore dataCore;
    /**
     * Configure the given {@link ServletContext} with any servlets, filters, listeners
     * context-params and attributes necessary for initialization.
     *
     * @param servletContext the {@code ServletContext} to initialize
     * @throws ServletException if any call against the given {@code ServletContext}
     *                          throws a {@code ServletException}
     */
    @Override public void onStartup(ServletContext servletContext) throws ServletException {
        EnumSet<DispatcherType> request = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
        for (Filter filter : filters) {
            String name = filter.getClass().getSimpleName();
            FilterRegistration.Dynamic dynamic = servletContext.addFilter(name, filter);

            String[] url = new String[1];
            url[0] = "/*";
            dynamic.addMappingForUrlPatterns(request,true,url);
        }


        List<FilterProperties> filters = robotFilterServletFilterProperties.getFilters();
        filters.sort(Comparator.comparing(FilterProperties::getOrder));

        Set<String> nameSet = new HashSet<>();
        for (FilterProperties filterProperties : filters) {
            String name = filterProperties.getName();
            if (StringUtils.isBlank(name)){
                throw new BeanCreationException("filter name is not null !!!");
            }



            Set<String> urlPattern = filterProperties.getUrlPattern();


            String className = filterProperties.getClassName();

            List<RuleProperties> rules = filterProperties.getRules();
            List<RuleProperties> collect = rules.stream().sorted(Comparator.comparing(RuleProperties::getInterval)).collect(Collectors.toList());

            for (RuleProperties properties : collect) {
                try {
                    Class<?> clazz = Class.forName(className);
                    RobotDecisionFilter bean = (RobotDecisionFilter) clazz.newInstance();
                    if (!RobotDecisionFilter.class.isAssignableFrom(clazz)){
                        throw new BeanCreationException("Class " + className + " must is RobotDecisionFilter subclass" );
                    }
                    bean.setFilterProperties(filterProperties);
                    bean.setApplicationContext(applicationContext);
                    bean.setLogUtils(logUtils);
                    bean.setDataCore(dataCore);
                    bean.setRuleProperties(properties);
                    String filterName = "robot.filter." + name + ".rule." + properties.getName();
                    if (nameSet.contains(filterName)){
                        throw new BeanCreationException("filter name repeat :" + name);
                    }
                    bean.setFilterName(filterName);
                    FilterRegistration.Dynamic dynamic = servletContext.addFilter(filterName, bean);

                    String[] url = new String[urlPattern.size()];
                    urlPattern.toArray(url);
                    dynamic.addMappingForUrlPatterns(request,true,url);

                    nameSet.add(filterName);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    throw new BeanCreationException(e.getMessage());
                }
            }

        }
    }
}
