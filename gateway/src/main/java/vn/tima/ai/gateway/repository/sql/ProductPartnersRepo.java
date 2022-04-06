package vn.tima.ai.gateway.repository.sql;


import vn.tima.ai.gateway.model.ProductPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductPartnersRepo extends JpaRepository<ProductPartner, String> {

    @Modifying
    @Transactional
    @Query("update ProductPartner pp set pp.permissionRoles = :permissionRoles " +
            "where pp.appId = :appId")
    void updatePermission(@Param("appId") String appId, @Param("permissionRoles") String permissionRoles);

    @Modifying
    @Transactional
    @Query("update ProductPartner pp set pp.isBlock = true where pp.appId = :appId")
    void blockAppId(@Param("appId") String appId);

    @Query("select pp from ProductPartner pp where pp.appId =:appId and pp.isBlock=false ")
    ProductPartner findPartnerActiveByAppId(@Param("appId") String appId);
}
