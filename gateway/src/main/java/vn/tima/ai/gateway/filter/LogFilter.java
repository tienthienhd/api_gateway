package vn.tima.ai.gateway.filter;

import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.handler.predicate.ReadBodyRoutePredicateFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR;
import static org.springframework.web.server.ServerWebExchange.LOG_ID_ATTRIBUTE;

import org.springframework.core.io.buffer.DataBuffer;

@RefreshScope
@Component
@Log4j2
public class LogFilter implements GlobalFilter, Ordered {

    @Autowired
    private ModifyResponseBodyGatewayFilterFactory modifyFilter;

    @Autowired
    private rewriteBody bodyRewrite;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("LogFilter");
        return chain.filter(exchange).map(ex -> {
            GatewayFilter delegate = modifyFilter.apply(new ModifyResponseBodyGatewayFilterFactory.Config()
                    .setRewriteFunction(byte[].class, byte[].class, bodyRewrite));
            delegate.filter(exchange, chain);

            return ex;
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Component
    public class rewriteBody implements RewriteFunction<byte[], byte[]> {

        @Override
        public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] body) {
            byte[] newBody = "New response".getBytes();

            return Mono.just(newBody);
        }
    }
}