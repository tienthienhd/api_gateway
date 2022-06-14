package vn.tima.ai.gateway.repository.sql;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import vn.tima.ai.gateway.model.ProductPartner;


@Repository
public interface ProductPartnersRepo extends ReactiveCrudRepository<ProductPartner, String> {

    Flux<ProductPartner> findByAppIdAndPermissionRoles(String appId, String permissionRoles);

    Flux<ProductPartner> findByAppId(String appId);

    Flux<ProductPartner> findByAppIdAndBlockFalse(String appId);
}
