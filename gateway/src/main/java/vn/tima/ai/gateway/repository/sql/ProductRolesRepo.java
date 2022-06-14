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

    @Query("SELECT * FROM product_feature_role pr ORDER BY pr.feature_regex_path")
    Flux<ProductRole> findAllSortASC();

    @Query("SELECT * FROM product_feature_role pr ORDER BY pr.feature_regex_path desc")
    Flux<ProductRole> findAllSortDESC();

    @Query("SELECT * FROM product_feature_role pr WHERE pr.feature_regex_path = :pathRegex")
    Mono<ProductRole> findByFeaturePathRegex(@Param("pathRegex") String pathRegex);

}
