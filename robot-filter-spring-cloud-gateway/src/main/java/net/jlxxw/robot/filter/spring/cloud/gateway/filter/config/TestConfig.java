package net.jlxxw.robot.filter.spring.cloud.gateway.filter.config;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2022-11-11 10:27 AM
 */
@Component
public class TestConfig {
    @Autowired
    GatewayProperties gatewayProperties;

    public  void main() {
        List<RouteDefinition> routes = gatewayProperties.getRoutes();
        List<FilterDefinition> filters = gatewayProperties.getDefaultFilters();
        
        for (RouteDefinition route : routes) {
            List<PredicateDefinition> predicates = route.getPredicates();
            for (PredicateDefinition predicate : predicates) {
                Map<String, String> args = predicate.getArgs();
                String path = args.get("Path");
                // 检查 value 和 过滤器配置是否匹配
                
                
            }
        }
        for (FilterDefinition filter : filters) {
            filter.getArgs();
        }
    }
}
