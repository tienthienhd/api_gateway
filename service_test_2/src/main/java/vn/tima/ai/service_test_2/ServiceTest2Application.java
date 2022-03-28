package vn.tima.ai.service_test_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ServiceTest2Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTest2Application.class, args);
    }

}
