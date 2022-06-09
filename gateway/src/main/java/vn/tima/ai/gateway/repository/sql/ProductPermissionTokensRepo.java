package vn.tima.ai.gateway.repository.sql;


import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.tima.ai.gateway.model.ProductPermissionToken;

import java.util.List;

@Repository
public interface ProductPermissionTokensRepo extends ReactiveCrudRepository<ProductPermissionToken, Integer> {

    Flux<ProductPermissionToken> findByAppId(String appId);

    Flux<ProductPermissionToken> findByToken(String token);

    @Query("SELECT ppt FROM ProductPermissionToken ppt WHERE ppt.expiredDate <= CURRENT_DATE OR ppt.isActive=false ")
    List<ProductPermissionToken> findTokenExpired();

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductPermissionToken ppt WHERE ppt.appId =:appId")
    Mono<Integer> deleteTokenByAppId(String appId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductPermissionToken ppt WHERE ppt.expiredDate <= CURRENT_DATE OR ppt.isActive=false")
    Mono<Integer> cleanTokenExpired();


}
