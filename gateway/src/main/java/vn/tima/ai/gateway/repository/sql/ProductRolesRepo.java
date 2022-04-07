package vn.tima.ai.gateway.repository.sql;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import vn.tima.ai.gateway.model.ProductRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface ProductRolesRepo extends JpaRepository<ProductRole, String> {

    @Query("select pr from ProductRole pr order by pr.featurePathRegex")
    List<ProductRole> findAllSortASC();

    @Query("select pr from ProductRole pr order by pr.featurePathRegex desc")
    List<ProductRole> findAllSortDESC();

}
