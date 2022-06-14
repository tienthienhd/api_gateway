package vn.tima.ai.gateway.model;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("product_partners")
@ToString
public class ProductPartner {

    @Id
    @Column("app_id")
    protected String appId;

    @Column("app_key")
    protected String appKey;

    @Column("permission")
    protected String permissionRoles;

    @Column("is_block")
    protected boolean isBlock;

    @Column("description")
    protected String description;

    @Column("token_accept_day")
    protected Integer tokenAcceptDay;

    public ProductPartner(String appId, String appKey, String permissionRoles, Integer tokenAcceptDay) {
        this.appId = appId;
        this.appKey = appKey;
        this.permissionRoles = permissionRoles;
        this.tokenAcceptDay = tokenAcceptDay;
    }

    public ProductPartner() {
    }
}
