package vn.tima.ai.security.repository.sql;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.tima.ai.security.model.ProductPartner;


@Repository
public interface ProductPartnersRepo extends ReactiveCrudRepository<ProductPartner, String> {

    Mono<ProductPartner> findByAppId(String appId);

    Mono<ProductPartner> findByAppIdAndBlockFalse(String appId);

}
