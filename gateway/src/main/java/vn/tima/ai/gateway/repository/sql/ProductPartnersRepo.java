package vn.tima.ai.gateway.repository.sql;


import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.tima.ai.gateway.model.ProductPartner;


@Repository
public interface ProductPartnersRepo extends ReactiveCrudRepository<ProductPartner, String> {

    @Modifying
    @Transactional
    @Query("update ProductPartner pp set pp.permissionRoles = :permissionRoles " +
            "where pp.appId = :appId")
    Mono<Integer> updatePermission(@Param("appId") String appId, @Param("permissionRoles") String permissionRoles);

    @Modifying
    @Transactional
    @Query("update ProductPartner pp set pp.isBlock = true where pp.appId = :appId")
    Mono<Integer> blockAppId(@Param("appId") String appId);

    @Query("SELECT pp FROM ProductPartner pp WHERE pp.appId =:appId AND pp.isBlock=false ")
    Flux<ProductPartner> findPartnerActiveByAppId(@Param("appId") String appId);
}
