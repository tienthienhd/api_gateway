package vn.tima.ai.gateway.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@Configuration
@EnableR2dbcRepositories(basePackages = "vn.tima.ai.common.*")
@EntityScan(basePackages = {"vn.tima.ai.common"})
@ComponentScan(basePackages = {"vn.tima.ai.common"})
public class ComponentGatewayConfig {

}
