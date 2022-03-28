package vn.tima.ai.service_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ServiceTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTestApplication.class, args);
    }

}
