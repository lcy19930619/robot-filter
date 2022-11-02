package net.jlxxw.robot.filter.servlet.runner;

import net.jlxxw.robot.filter.config.properties.FilterProperties;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * servlet filter auto configuration
 */
@Component
public class RobotServletFilterRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(RobotServletFilterRunner.class);
    @Autowired
    private RobotFilterProperties robotFilterProperties;
    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<FilterProperties> filters = robotFilterProperties.getFilters();
        filters.sort(Comparator.comparing(FilterProperties::getOrder));

        if (beanFactory instanceof DefaultListableBeanFactory){
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)beanFactory;

            for (FilterProperties filterProperties : filters) {
                String name = filterProperties.getName();
                String className = filterProperties.getClassName();
                Set<String> urlPattern = filterProperties.getUrlPattern();
                // todo param check to lcy 2022/11/05 done

                Class clazz = Class.forName(className);
                if (!Filter.class.isAssignableFrom(clazz)){
                     throw new BeanCreationException(className + " not is Filter sub class!!!");
                }
                FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
                filterRegistrationBean.setUrlPatterns(urlPattern);
                filterRegistrationBean.setEnabled(filterProperties.isEnable());
                Object bean;
                try {
                    bean = defaultListableBeanFactory.getBean(clazz, className);
                    filterRegistrationBean.setFilter((Filter)bean);
                }catch (NoSuchBeanDefinitionException e){
                    // ignore not fount bean,do create Bean
                     bean = defaultListableBeanFactory.createBean(clazz);
                    filterRegistrationBean.setFilter((Filter)bean);
                }
                String beanName = "robot.filter." + name;
                defaultListableBeanFactory.registerSingleton(beanName + name ,bean);
                logger.info("register servlet robot filter :",beanName);

            }
        }


    }
}
