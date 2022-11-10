package net.jlxxw.robot.filter.web;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.ui.UiProperties;
import net.jlxxw.robot.filter.core.check.IpCheck;
import net.jlxxw.robot.filter.servlet.utils.IpUtils;
import net.jlxxw.robot.filter.web.filter.SwaggerSecurityFilter;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author chunyang.leng
 * @date 2022-11-09 6:01 PM
 */
@EnableAspectJAutoProxy
@ComponentScan("net.jlxxw.robot.filter.web")
public class RobotFilterWebAutoConfiguration implements ServletContextInitializer {

    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private SpringDocConfigProperties springDocConfigProperties;
    @Autowired
    private SwaggerUiConfigProperties swaggerUiConfigProperties;

    @Autowired
    private UiProperties uiProperties;
    @Autowired
    private IpUtils ipUtils;
    @Autowired
    private IpCheck ipCheck;
    @Autowired
    private LogUtils logUtils;

    /**
     * Configure the given {@link ServletContext} with any servlets, filters, listeners
     * context-params and attributes necessary for initialization.
     *
     * @param servletContext the {@code ServletContext} to initialize
     * @throws ServletException if any call against the given {@code ServletContext}
     *                          throws a {@code ServletException}
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        SpringDocConfigProperties.ApiDocs docs = springDocConfigProperties.getApiDocs();
        String path1 = docs.getPath();

        String path2 = swaggerUiConfigProperties.getPath();
        System.out.println();
        if (StringUtils.isBlank(path2)) {
            path2 = "/swagger-ui/**/*";
        }
        SpringDocConfigProperties.Webjars webjars = springDocConfigProperties.getWebjars();
        String path3 = webjars.getPrefix();

        Set<String> list = new HashSet<>();
        list.add(path1);
        list.add(path1 + "/**/*");
        list.add(path2);
        list.add(path2 + "/**/*");
        list.add(path3);
        list.add(path3 + "/**/*");
        list.add( "/swagger-ui/**/*");

        SwaggerSecurityFilter filter = new SwaggerSecurityFilter();
        filter.setIpUtils(ipUtils);
        filter.setIpCheck(ipCheck);
        filter.setUiProperties(uiProperties);
        filter.setLogUtils(logUtils);
        FilterRegistration.Dynamic dynamic = servletContext.addFilter("swagger-security-filter", filter);
        String[] temp = new String[list.size()];

        List<String> collect = list.stream().map(prefix -> {
            if (!prefix.startsWith("/")) {
                return "/" + prefix;
            }
            return prefix;
        }).collect(Collectors.toList());

        collect.toArray(temp);
        EnumSet<DispatcherType> types = EnumSet.of(DispatcherType.REQUEST);
        dynamic.addMappingForUrlPatterns(types,true,temp);

    }
}
