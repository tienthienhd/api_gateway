package vn.tima.ai.security.model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "product_partners")
@ToString
public class ProductPartner {

    @Id
    @Column(name = "app_id")
    protected String appId;
    @Column(name = "app_key")
    protected String appKey;
    @Column(name = "permission")
    protected String permissionRoles;
    @Column(name = "is_block")
    protected boolean isBlock;
    @Column(name = "description")
    protected String description;
    @Column(name = "token_accept_day")
    protected Integer tokenAcceptDay;

    public ProductPartner(String appId, String appKey, String permissionRoles, Integer tokenAcceptDay){
        this.appId=appId;
        this.appKey=appKey;
        this.permissionRoles=permissionRoles;
        this.tokenAcceptDay=tokenAcceptDay;
    }

    public ProductPartner(){}
}
