package net.jlxxw.robot.filter.servlet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.filter.FilterProperties;
import net.jlxxw.robot.filter.config.properties.filter.servlet.RobotFilterServletFilterProperties;
import net.jlxxw.robot.filter.servlet.filter.decision.RobotDecisionFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * servlet filter auto configuration
 */
@ServletComponentScan(basePackages = {
    "net.jlxxw.robot.filter.servlet.filter.global",
    "net.jlxxw.robot.filter.servlet.filter.response"})
@ConditionalOnClass(Servlet.class)
@ComponentScan("net.jlxxw.robot.filter.servlet")
@Configuration
public class RobotServletFilterAutoConfiguration implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(RobotServletFilterAutoConfiguration.class);
    @Autowired
    private RobotFilterServletFilterProperties robotFilterServletFilterProperties;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private LogUtils logUtils;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<FilterProperties> filters = robotFilterServletFilterProperties.getFilters();
        filters.sort(Comparator.comparing(FilterProperties::getOrder));

        Set<String> nameSet = new HashSet<>();
        if (beanFactory instanceof DefaultListableBeanFactory){
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)beanFactory;
            for (FilterProperties filterProperties : filters) {
                String name = filterProperties.getName();
                if (StringUtils.isBlank(name)){
                    throw new BeanCreationException("filter name is not null !!!");
                }
                if (nameSet.contains(name)){
                    throw new BeanCreationException("filter name repeat :" + name);
                }
                nameSet.add(name);

                Set<String> urlPattern = filterProperties.getUrlPattern();
                FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
                filterRegistrationBean.setUrlPatterns(urlPattern);
                filterRegistrationBean.setEnabled(filterProperties.isEnable());
                filterRegistrationBean.setOrder(filterProperties.getOrder());
                String beanName = "robot.filter." + name;

                RobotDecisionFilter bean;
                String className = filterProperties.getClassName();
                Class<?> clazz = Class.forName(className);
                if (!RobotDecisionFilter.class.isAssignableFrom(clazz)){
                    throw new BeanCreationException("Class " + className + " must is RobotDecisionFilter subclass" );
                }
                try {
                    bean = (RobotDecisionFilter)defaultListableBeanFactory.getBean(beanName,clazz);
                    if (Objects.isNull(bean.getFilterProperties())){
                        bean.setFilterProperties(filterProperties);
                    }
                    filterRegistrationBean.setFilter(bean);
                }catch (NoSuchBeanDefinitionException e){
                    // ignore not fount bean,do create Bean
                    GenericBeanDefinition definition = new GenericBeanDefinition();
                    definition.setAutowireCandidate(true);
                    definition.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
                    definition.setBeanClass(clazz);
                    definition.setAttribute("filterProperties",filterProperties);
                    defaultListableBeanFactory.registerBeanDefinition(beanName, definition);

                    bean = defaultListableBeanFactory.getBean(beanName,RobotDecisionFilter.class);
                    filterRegistrationBean.setFilter(bean);
                }
                defaultListableBeanFactory.registerSingleton(beanName + name ,bean);
                logUtils.info(logger,"register servlet robot filter :{}",beanName);
            }
        }


    }
}
