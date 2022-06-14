package vn.tima.ai.security.repository.sql;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import vn.tima.ai.security.model.ProductRole;


@Repository
public interface ProductRolesRepo extends ReactiveCrudRepository<ProductRole, Integer> {
    Flux<ProductRole> findByPath(String path);

}
