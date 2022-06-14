package vn.tima.ai.gateway.model;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Data
@Table("partners_permission")
@ToString
public class ProductPermissionToken implements Serializable {
    static final long serialVersionUID = -681492884005033L;

    @Id
    @Column("id")
    protected Integer id;

    @Column("token")
    protected String token;

    @Column("app_id")
    protected String appId;

    @Column("is_active")
    protected boolean isActive;

    @Column("expired_date")
    protected Date expiredDate;

    public ProductPermissionToken(String token, String appId, Date expiredDate) {
        this.token = token;
        this.appId = appId;
        this.expiredDate = expiredDate;
        this.isActive = true;
    }

    public ProductPermissionToken() {

    }
}
