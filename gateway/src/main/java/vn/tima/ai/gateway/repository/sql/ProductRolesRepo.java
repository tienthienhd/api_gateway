package vn.tima.ai.gateway.repository.sql;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.tima.ai.gateway.model.ProductRole;

@Repository
public interface ProductRolesRepo extends ReactiveCrudRepository<ProductRole, Integer> {

    @Query("SELECT pr FROM ProductRole pr ORDER BY pr.featurePathRegex")
    Flux<ProductRole> findAllSortASC();

    @Query("SELECT pr FROM ProductRole pr ORDER BY pr.featurePathRegex desc")
    Flux<ProductRole> findAllSortDESC();

    @Query("SELECT pr FROM ProductRole pr WHERE pr.featurePathRegex = :pathRegex")
    Mono<ProductRole> findByFeaturePathRegex(@Param("pathRegex") String pathRegex);

}
