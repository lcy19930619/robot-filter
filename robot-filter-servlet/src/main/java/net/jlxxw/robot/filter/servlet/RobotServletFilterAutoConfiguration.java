package net.jlxxw.robot.filter.servlet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import net.jlxxw.robot.filter.servlet.template.AbstractFilterTemplate;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * servlet filter auto configuration
 */
@ConditionalOnClass(Servlet.class)
@ComponentScan("net.jlxxw.robot.filter.servlet")
@Configuration
public class RobotServletFilterAutoConfiguration implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(RobotServletFilterAutoConfiguration.class);
    @Autowired
    private RobotFilterProperties robotFilterProperties;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private LogUtils logUtils;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<FilterProperties> filters = robotFilterProperties.getFilters();
        filters.sort(Comparator.comparing(FilterProperties::getOrder));

        Set<String> nameSet = new HashSet<>();
        Set<String> classNameSet = new HashSet<>();
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

                String className = filterProperties.getClassName();
                if (StringUtils.isBlank(className)){
                    throw new BeanCreationException("className name is not null !!!");
                }
                if (nameSet.contains(className)){
                    throw new BeanCreationException("className name repeat :" + name);
                }
                classNameSet.add(className);

                Set<String> urlPattern = filterProperties.getUrlPattern();

                Class<?> clazz = Class.forName(className);
                if (!AbstractFilterTemplate.class.isAssignableFrom(clazz)){
                     throw new BeanCreationException(className + " not is AbstractFilterTemplate sub class!!!");
                }
                FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
                filterRegistrationBean.setUrlPatterns(urlPattern);
                filterRegistrationBean.setEnabled(filterProperties.isEnable());
                String beanName = "robot.filter." + name;

                AbstractFilterTemplate bean;
                try {
                    bean = (AbstractFilterTemplate)defaultListableBeanFactory.getBean(clazz, className);
                    if (Objects.isNull(bean.getFilterProperties())){
                        bean.setFilterProperties(filterProperties);
                    }
                    filterRegistrationBean.setFilter(bean);
                }catch (NoSuchBeanDefinitionException e){
                    // ignore not fount bean,do create Bean
                    GenericBeanDefinition definition = new GenericBeanDefinition();
                    definition.setAutowireCandidate(true);
                    definition.setBeanClassName(className);
                    definition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
                    definition.setBeanClass(clazz);
                    definition.setAttribute("filterProperties",filterProperties);
                    defaultListableBeanFactory.registerBeanDefinition(beanName, definition);

                    bean = (AbstractFilterTemplate)defaultListableBeanFactory.getBean(clazz, className);
                    filterRegistrationBean.setFilter(bean);
                }
                defaultListableBeanFactory.registerSingleton(beanName + name ,bean);
                logUtils.info(logger,"register servlet robot filter :{}",beanName);
            }
        }


    }
}
