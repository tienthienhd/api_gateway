package vn.tima.ai.gateway.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.tima.ai.gateway.model.ProductRole;

import java.util.List;

@Repository
public interface ProductRolesRepo extends JpaRepository<ProductRole, String> {

    @Query("select pr from ProductRole pr order by pr.featurePathRegex")
    List<ProductRole> findAllSortASC();

    @Query("select pr from ProductRole pr order by pr.featurePathRegex desc")
    List<ProductRole> findAllSortDESC();

    @Query("select pr from ProductRole pr where pr.featurePathRegex = :pathRegex")
    ProductRole findByFeaturePathRegex(@Param("pathRegex") String pathRegex);

}
