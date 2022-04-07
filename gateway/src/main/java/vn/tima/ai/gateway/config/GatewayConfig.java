package vn.tima.ai.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.tima.ai.gateway.filter.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("FIRST-SERVICE", r -> r.path("/**")
//                        .filters(f -> f.filter(filter).filter(filter2))
                        .filters(f -> f.filter(filter))
                        .uri("lb://FIRST-SERVICE"))

                .build();
    }

}