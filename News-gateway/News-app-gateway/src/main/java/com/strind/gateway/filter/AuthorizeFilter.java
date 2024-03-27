package com.strind.gateway.filter;

import com.strind.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author strind
 * @version 1.0
 * @description 全局过滤器，进行授权认证
 * @date 2024/3/22 9:59
 */
@Component
@Order(-1)
@Slf4j
public class AuthorizeFilter implements GlobalFilter {

    /**
     * @param exchange 上下文
     * @param chain 过滤器链
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取request 和 response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        log.info("url: {}",request.getURI());

        // 判断是否是登录
        if (request.getURI().getPath().contains("/login")) {
            // 放行
            return chain.filter(exchange);
        }

        // 获取token
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 判断token是否有效
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            // 是否过期
            int result = AppJwtUtil.verifyToken(claimsBody);
            if (result == 1 || result == 2){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            // 获取用户信息
            Object userId = claimsBody.get("id");
            // 放入header交给下游
            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.add("userId", userId + "");
            }).build();
            // 重置请求
            exchange.mutate().request(serverHttpRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
        return chain.filter(exchange);
    }
}
