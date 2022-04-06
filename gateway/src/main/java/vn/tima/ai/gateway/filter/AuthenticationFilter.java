package vn.tima.ai.gateway.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
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

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();


        if (this.isAuthMissing(request))
            return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

        final String token = this.getAuthHeader(request);

        try {
            jwtUtil.validateToken(token);
        } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
            return this.onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

//        this.populateRequestWithHeaders(exchange, token);

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
}