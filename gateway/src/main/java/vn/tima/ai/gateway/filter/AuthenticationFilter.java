package vn.tima.ai.gateway.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.tima.ai.gateway.exception.JwtTokenMalformedException;
import vn.tima.ai.gateway.exception.JwtTokenMissingException;
import vn.tima.ai.gateway.utils.JwtUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import vn.tima.ai.gateway.model.ProductRole;
import vn.tima.ai.gateway.service.SecurityAdminService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Null;


@RefreshScope
@Component
@Log4j2
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public SecurityAdminService securityAdminService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info(request);

        String[] split_path = request.getPath().toString().split("/");
        String pathRegex = "/";
        String childPathRegex = "/";

        for (int i = 1; i < split_path.length; i++) {
            childPathRegex += split_path[i];
            childPathRegex += "/";
            ProductRole role = securityAdminService.getRoleIdByPath(childPathRegex + "**");
            if (role != null) {
                pathRegex = childPathRegex + "**";
            }
        }
        ProductRole role = securityAdminService.getRoleIdByPath(pathRegex);
        log.info("ProductRole: " + role);
        log.info("RoleId: " + role.getRoleId());

        if (role != null) {
            if (role.getRoleId().contains("PUBLIC")) {
                return chain.filter(exchange);
            } else {
                if (this.isAuthMissing(request)) {
                    return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
                } else {
                    try {
                        final String token = this.getAuthHeader(request);
                        Claims claims = jwtUtil.validateToken(token);
                        String authorities = String.valueOf(claims.get("authorities"));
                        log.info("claims: " + claims);

                        if (!authorities.contains(role.getRoleId())) {
                            return this.onError(exchange, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
                        }
                    } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                        return this.onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
                    }
                }
            }
//        this.populateRequestWithHeaders(exchange, token);
        }


        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
//        Claims claims = jwtUtil.parseToken(token);
        Claims claims = jwtUtil.parseJwt(token);
        String sub = String.valueOf(claims.get("sub"));
        String authorities = String.valueOf(claims.get("authorities"));
        exchange.getRequest().mutate()
                .header("sub", String.valueOf(claims.get("sub")))
//                .header("authorities", String.valueOf(claims.get("authorities")))
                .build();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}