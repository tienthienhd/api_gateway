package vn.tima.ai.gateway.repository.sql;

import vn.tima.ai.gateway.model.ProductPermissionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductPermissionTokensRepo extends JpaRepository<ProductPermissionToken, Integer> {


    @Query("select ppt from ProductPermissionToken ppt where ppt.appId =:appId")
    List<ProductPermissionToken> findTokenByAppId(@Param("appId") String appId);

    @Query("select ppt from ProductPermissionToken ppt where ppt.token =:token")
    ProductPermissionToken findToken(@Param("token") String token);

    @Query("select ppt from ProductPermissionToken ppt where ppt.expiredDate <= CURRENT_DATE or ppt.isActive=false ")
    List<ProductPermissionToken> findTokenExpired();

    @Modifying
    @Transactional
    @Query("delete from ProductPermissionToken ppt where ppt.appId =:appId")
    void deleteTokenByAppId(String appId);

    @Modifying
    @Transactional
    @Query("delete from ProductPermissionToken ppt where ppt.expiredDate <= CURRENT_DATE or ppt.isActive=false")
    void cleanTokenExpired();


}
