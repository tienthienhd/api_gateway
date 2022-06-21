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
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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

import org.springframework.security.core.userdetails.User;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

import static vn.tima.ai.gateway.filter.jwt.JwtTokenAuthenticationFilter.HEADER_PREFIX;

@Configuration
@EnableWebFluxSecurity
@Log4j2

public class SecurityConfig {

    @Autowired
    public ProductRolesRepo productRolesRepo;


    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JwtTokenProvider tokenProvider,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {

        Flux<ProductRole> productRoles = productRolesRepo.findAllSortDESC();

        List<ProductRole> roles = new ArrayList<>();

        Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeCustomizer = authorizeExchangeSpec -> {
            productRoles.log().map(s -> {
                roles.add(s);
                return s;
            }).blockLast();

            for(int i=0; i< roles.size(); i++){
                ProductRole thisRole = roles.get(i);

                if(String.valueOf(ProductRole.FixRole.PUBLIC).equals(thisRole.getRoleId())){
                    log.info("Gateway set role: "+ thisRole.getFeaturePathRegex() + " - [PUBLIC]");
                    authorizeExchangeSpec.pathMatchers(thisRole.getFeaturePathRegex()).permitAll();
                } else {
                    List<String> roleIDs = new ArrayList<>();
                    for(int j = i; j< roles.size(); j++){
                        ProductRole nextRole = roles.get(j);
                        if(thisRole.isChildRoleOf(nextRole) && !String.valueOf(ProductRole.FixRole.PUBLIC).equals(nextRole.getRoleId()))
                            roleIDs.add(nextRole.getRoleId());
                    }
                    log.info("Gateway set role: " + thisRole.getFeaturePathRegex() + " - " + roleIDs.toString());
                    authorizeExchangeSpec.pathMatchers(thisRole.getFeaturePathRegex()).hasAnyRole(roleIDs.toArray(new String[roleIDs.size()]));
                }
            }


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


    @Bean
    public ReactiveUserDetailsService userDetailsService(ProductPartnersRepo productPartnersRepo) {
        return appId -> productPartnersRepo.findPartnerActiveByAppId(appId)
                .map(u -> User
                        .withUsername(u.getAppId()).password(u.getAppKey())
                        .authorities(convertAuthorities(u).toArray(new String[0]))
                        .accountExpired(!u.isBlock())
                        .credentialsExpired(!u.isBlock())
                        .disabled(!u.isBlock())
                        .accountLocked(!u.isBlock())
                        .build()
                );
    }


    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public List<String> convertAuthorities(ProductPartner productPartner){
        String[] rolesPermission= productPartner.getPermissionRoles().split(",");
        List<String> grantedAuthorityList = new ArrayList<>();
        for(String rolePermission: rolesPermission){
            grantedAuthorityList.add("ROLE_"+rolePermission.trim());
        }
        log.info("grantedAuthorityList: " + grantedAuthorityList);
        return grantedAuthorityList;
    }

}
