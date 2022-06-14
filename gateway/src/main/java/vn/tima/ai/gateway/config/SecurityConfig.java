package vn.tima.ai.gateway.config;

import com.sun.org.apache.xml.internal.serializer.SerializerTrace;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import reactor.core.publisher.Flux;


import reactor.core.publisher.Mono;
import vn.tima.ai.gateway.filter.jwt.JwtTokenAuthenticationFilter;
import vn.tima.ai.gateway.filter.jwt.JwtTokenProvider;
import vn.tima.ai.gateway.repository.sql.ProductPartnersRepo;
import vn.tima.ai.gateway.repository.sql.ProductRolesRepo;
import vn.tima.ai.gateway.model.ProductRole;


import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@Log4j2

public class SecurityConfig {

    @Autowired
    public ProductRolesRepo productRolesRepo;


    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JwtTokenProvider tokenProvider) {

        Flux<ProductRole> productRoles = productRolesRepo.findAll();
        ArrayList<String> roleList = new ArrayList<>();

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        productRoles.log().subscribe(s -> http.authorizeExchange(it -> {
                            if (s.getRoleId().equals(ProductRole.FixRole.PUBLIC.toString())) {
                                it.pathMatchers(s.getFeaturePathRegex()).permitAll();
                            } else {
                                it.pathMatchers(s.getFeaturePathRegex()).authenticated();
                            }
                        }
                )
        );
//        http.authorizeExchange(it -> it.anyExchange().permitAll());
        http.addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC);
        return http.build();
    }

    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication,
                                                               AuthorizationContext context) {

        return authentication
                .map(a -> context.getVariables().get("user").equals(a.getName()))
                .map(AuthorizationDecision::new);

    }

//    @Bean
//    public ReactiveUserDetailsService userDetailsService(ProductPartnersRepo productPartner) {
//
//        return appId -> productPartner.findPartnerActiveByAppId(appId)
//                .map(u -> ProductPartner
//                        .withUsername(u.getUsername()).password(u.getPassword())
//                        .authorities(u.getRoles().toArray(new String[0]))
//                        .accountExpired(!u.isActive())
//                        .credentialsExpired(!u.isActive())
//                        .disabled(!u.isActive())
//                        .accountLocked(!u.isActive())
//                        .build()
//                );
//    }

//    @Bean
//    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
//                                                                       PasswordEncoder passwordEncoder) {
//        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
//        authenticationManager.setPasswordEncoder(passwordEncoder);
//        return authenticationManager;
//    }

}
