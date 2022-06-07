package vn.tima.ai.gateway.filter;

import com.netflix.discovery.converters.Auto;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import sun.rmi.runtime.Log;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

import vn.tima.ai.gateway.exception.JwtTokenMalformedException;
import vn.tima.ai.gateway.exception.JwtTokenMissingException;
import vn.tima.ai.gateway.model.LogRequests;
import vn.tima.ai.gateway.repository.sql.LogRequestsRepo;
import vn.tima.ai.gateway.utils.JwtUtil;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Set<String> LOGGABLE_CONTENT_TYPES = new HashSet<>(Arrays.asList(
            MediaType.APPLICATION_JSON_VALUE.toLowerCase(),
            MediaType.APPLICATION_JSON_UTF8_VALUE.toLowerCase(),
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.TEXT_XML_VALUE
    ));

    @Autowired
    LogRequestsRepo logRequestsRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange, GatewayFilterChain chain
    ) {
        Claims claims = null;
        try {
            if(exchange.getRequest().getHeaders().get("Authorization") != null){
                String authorization = exchange.getRequest().getHeaders().get("Authorization").get(0);
                log.info("authorization" + authorization);
                claims = jwtUtil.validateToken(authorization);
                String app_id = String.valueOf(claims.get("sub"));
                exchange.getRequest().mutate().header("app-id", app_id).build();

                LogRequests logRequests = new LogRequests();
                // First we get here request in exchange
                var requestMutated = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        var requestLogger = new Logger(getDelegate());
                        if(LOGGABLE_CONTENT_TYPES.contains(String.valueOf(getHeaders().getContentType()).toLowerCase())) {
                            return super.getBody().map(ds -> {
                                requestLogger.appendBody(ds.asByteBuffer());

                                logRequests.setRequest_id(exchange.getRequest().getId());
                                logRequests.setUri(String.valueOf(exchange.getRequest().getURI()));
                                logRequests.setMethod(exchange.getRequest().getMethod().toString());
                                Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
                                logRequests.setTimestamp(timeStamp.toString());
                                logRequests.setPath(exchange.getRequest().getPath().toString());
                                logRequests.setApp_id(app_id);
                                String request_body = StandardCharsets.UTF_8.decode(ds.asByteBuffer()).toString();
                                logRequests.setInput_body(request_body);
                                logRequestsRepo.save(logRequests);

                                return ds;
                            }).doFinally((s) -> requestLogger.log());
                        } else {
                            requestLogger.log();

                            logRequests.setRequest_id(exchange.getRequest().getId());
                            logRequests.setUri(String.valueOf(exchange.getRequest().getURI()));
                            logRequests.setMethod(exchange.getRequest().getMethod().toString());
                            Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
                            logRequests.setTimestamp(timeStamp.toString());
                            logRequests.setPath(exchange.getRequest().getPath().toString());
                            logRequests.setApp_id(app_id);

                            logRequestsRepo.save(logRequests);
                            return super.getBody();
                        }

                    }
                };

                var responseMutated = new ServerHttpResponseDecorator(exchange.getResponse()) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        var responseLogger = new Logger(getDelegate());
                        LogRequests logRequests1 = logRequestsRepo.findByRequest_id(exchange.getRequest().getId());
                        if(LOGGABLE_CONTENT_TYPES.contains(String.valueOf(getHeaders().getContentType()).toLowerCase())) {
                            return join(body).flatMap(db -> {
                                responseLogger.appendBody(db.asByteBuffer());
                                responseLogger.log();
                                String response_body = StandardCharsets.UTF_8.decode(db.asByteBuffer()).toString();

                                if (logRequests1 != null){
                                    logRequests1.setResponse(response_body);
                                    logRequests1.setStatus_code(exchange.getResponse().getStatusCode().toString());
                                    logRequestsRepo.save(logRequests1);
                                }
                                return getDelegate().writeWith(Mono.just(db));
                            });
                        } else {
                            responseLogger.log();
                            if (logRequests1 != null){
                                logRequests1.setStatus_code(exchange.getResponse().getStatusCode().toString());
                                logRequestsRepo.save(logRequests1);
                            }
                            return getDelegate().writeWith(body);
                        }

                    }
                };
                return chain.filter(exchange.mutate().request(requestMutated).response(responseMutated).build());
            }
        } catch (JwtTokenMalformedException e) {
            e.printStackTrace();
        } catch (JwtTokenMissingException e) {
            e.printStackTrace();
        }



        return chain.filter(exchange);
    }

    private Mono<? extends DataBuffer> join(Publisher<? extends DataBuffer> dataBuffers) {
        Assert.notNull(dataBuffers, "'dataBuffers' must not be null");
        return Flux.from(dataBuffers)
                .collectList()
                .filter((list) -> !list.isEmpty())
                .map((list) -> list.get(0).factory().join(list))
                .doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private static class Logger {
        private StringBuilder sb = new StringBuilder();

        Logger(ServerHttpResponse response) {
            sb.append("\n");
            sb.append("---- Response -----").append("\n");
            sb.append("Headers      :").append(response.getHeaders().toSingleValueMap()).append("\n");
            sb.append("Status code  :").append(response.getStatusCode()).append("\n");
            sb.append("Content Length  :").append(response.getHeaders().getContentLength()).append("\n");

        }

        Logger(ServerHttpRequest request) {
            sb.append("\n");
            sb.append("---- Request -----").append("\n");
            sb.append("Headers      :").append(request.getHeaders().toSingleValueMap()).append("\n");
            sb.append("Method       :").append(request.getMethod()).append("\n");
            sb.append("Client       :").append(request.getRemoteAddress()).append("\n");
        }


        void appendBody(ByteBuffer byteBuffer) {
            sb.append("Body         :").append(StandardCharsets.UTF_8.decode(byteBuffer)).append("\n");
        }

        void log() {
            sb.append("-------------------").append("\n");
            log.info(sb.toString());
        }

    }
}