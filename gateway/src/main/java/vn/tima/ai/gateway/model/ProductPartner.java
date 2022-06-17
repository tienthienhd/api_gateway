package vn.tima.ai.gateway.model;


import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "product_partners")
@ToString
public class ProductPartner {

    @Id
    @Column(name = "app_id")
    public String appId;

    @Column(name = "app_key")
    public String appKey;

    @Column(name = "permission")
    public String permissionRoles;

    @Column(name = "is_block")
    public boolean block;

    @Column(name = "description")
    public String description;

    @Column(name = "token_accept_day")
    public Integer tokenAcceptDay;

    public ProductPartner(String appId, String appKey, String permissionRoles, Integer tokenAcceptDay) {
        this.appId = appId;
        this.appKey = appKey;
        this.permissionRoles = permissionRoles;
        this.tokenAcceptDay = tokenAcceptDay;
    }

    public ProductPartner() {
    }
}
