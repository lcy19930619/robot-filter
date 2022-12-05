package net.jlxxw.robot.filter.spring.cloud.gateway.filter;

import java.nio.charset.StandardCharsets;
import net.jlxxw.robot.filter.config.properties.filter.FilterProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author chunyang.leng
 * @date 2022-11-11 10:17 AM
 */
@Component
public class Test extends AbstractGatewayFilterFactory<FilterProperties> {

    private static final Logger log = LoggerFactory.getLogger(Test.class);

    @Override
    public GatewayFilter apply(FilterProperties config) {
        return (exchange, chain) -> {
            String[] checkHeaderArr = getCheckHeaderArr(config);
            // 没有配置，进入下一个filter
            if (checkHeaderArr == null){
                log.info("check header config is null");
                return chain.filter(exchange);
            }
            log.info("requestUri:【{}】 校验 header", exchange.getRequest().getURI().toString());
            // 获取请求的所有header
            HttpHeaders headers = exchange.getRequest().getHeaders();
            for (String headerName : checkHeaderArr) {
                if (StringUtils.isBlank(headerName)){
                    continue;
                }
                // 如果要被校验的header不存在，直接编写响应信息给接口调用方
                if (!headers.containsKey(headerName)) {
                    return writeParamErrorResponse(exchange, headerName + "不能为空");
                }
            }
            log.info("requestUri:【{}】 校验 header 通过", exchange.getRequest().getURI().toString());
            // 校验通过，进入下一个filter
            return chain.filter(exchange);
        };
    }

    /**
     * 处理失败时的返回
     */
    private Mono<Void> writeParamErrorResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(message.getBytes(StandardCharsets.UTF_8));
        }));
    }

    /**
     * 从Config中获取配置
     */
    private String[] getCheckHeaderArr(FilterProperties config) {
        String[] checkHeaderArr = null;

        return checkHeaderArr;
    }

}
