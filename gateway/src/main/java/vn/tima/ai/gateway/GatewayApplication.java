package vn.tima.ai.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import reactor.netty.http.client.HttpClient;
import vn.tima.ai.gateway.utils.JwtUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication {

    public static void main(String[] args) {

//        JwtUtil jwt_util = new JwtUtil();
//
//        List authorities = Arrays.asList(new String[]{"ROLE_amazing_crawler","ROLE_appraisal_auto_check","ROLE_auto_moj_secured_transfer"});
//        String gen_token = jwt_util.generateToken("ai_super_admin_test",
//                authorities,86400000000L);
//        System.out.println("TOKEN: "+gen_token);

        SpringApplication.run(GatewayApplication.class, args);
    }

}


