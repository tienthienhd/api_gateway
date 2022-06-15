package vn.tima.ai.gateway.config;

import com.sun.org.apache.xml.internal.serializer.SerializerTrace;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;


import reactor.core.publisher.Mono;
import vn.tima.ai.gateway.filter.jwt.JwtTokenAuthenticationFilter;
import vn.tima.ai.gateway.filter.jwt.JwtTokenProvider;
import vn.tima.ai.gateway.model.ProductPartner;
import vn.tima.ai.gateway.repository.sql.ProductPartnersRepo;
import vn.tima.ai.gateway.repository.sql.ProductRolesRepo;
import vn.tima.ai.gateway.model.ProductRole;


import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static vn.tima.ai.gateway.filter.jwt.JwtTokenAuthenticationFilter.HEADER_PREFIX;

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

        Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeCustomizer = authorizeExchangeSpec -> {
            productRoles.log().map(s -> {
                if (String.valueOf(ProductRole.FixRole.PUBLIC).equals(s.getRoleId())) {
                    authorizeExchangeSpec.pathMatchers(s.getFeaturePathRegex()).permitAll();
                }else {
//                    authorizeExchangeSpec.pathMatchers(s.getFeaturePathRegex()).access((authentication, context) ->
//                        authentication.map(a -> {
//                                    String token = resolveToken(context.getExchange().getRequest());
//                                    validateToken(token);
//                                    log.info("111111111111: {}", token);
//                                    log.info("222222222222: {}", context.getExchange().getRequest().getPath());
//                                    log.info("444444444444: {}", context.getExchange().getRequest().getHeaders());
//                                    log.info("333333333333: {}", String.valueOf(context.getExchange().getRequest().getPath()).contains(s.getRoleId()));
//                                    return String.valueOf(context.getExchange().getRequest().getPath()).contains(s.getRoleId());
//                        })
//                                .map(AuthorizationDecision::new)
//                    );
                    authorizeExchangeSpec.pathMatchers(s.getFeaturePathRegex()).authenticated();
                }
                return s;
            }).blockLast();
            authorizeExchangeSpec.anyExchange().denyAll();
        };

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
//                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(authorizeExchangeCustomizer)
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();

    }

    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication,
                                                               AuthorizationContext context) {

        return authentication
                .map(a -> context.getVariables().get("user").equals(a.getName()))
                .map(AuthorizationDecision::new);

    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key secretKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        try {

            Jws<Claims> claims = Jwts
                    .parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token);
            //  parseClaimsJws will check expiration date. No need do here.
            log.info("expiration date: {}", claims.getBody().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
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
