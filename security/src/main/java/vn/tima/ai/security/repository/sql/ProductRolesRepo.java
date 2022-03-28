package vn.tima.ai.security.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.tima.ai.security.model.ProductRole;

import java.util.List;

public interface ProductRolesRepo extends JpaRepository<ProductRole, String> {

    @Query("select pr from ProductRole pr order by pr.featurePathRegex")
    List<ProductRole> findAllSortASC();

    @Query("select pr from ProductRole pr order by pr.featurePathRegex desc")
    List<ProductRole> findAllSortDESC();

}
