package vn.tima.ai.gateway.repository.sql;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.tima.ai.gateway.model.ProductPermissionToken;

@Repository
public interface ProductPermissionTokensRepo extends ReactiveCrudRepository<ProductPermissionToken, Integer> {

    Flux<ProductPermissionToken> findByAppId(String appId);

    Flux<ProductPermissionToken> findByToken(String token);

    Flux<ProductPermissionToken> findByActiveFalse();

    Mono<Integer> deleteByAppId(String appId);

    Mono<Integer> deleteByActiveFalse();

}
