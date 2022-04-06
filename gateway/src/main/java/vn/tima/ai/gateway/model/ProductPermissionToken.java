package vn.tima.ai.gateway.model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "partners_permission")
@ToString
public class ProductPermissionToken implements Serializable {
    static final long serialVersionUID = -681492884005033L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;
    @Column(name = "token", columnDefinition = "TEXT")
    protected String token;
    @Column(name = "app_id")
    protected String appId;
    @Column(name = "is_active")
    protected boolean isActive;
    @Column(name = "expired_date")
    protected Date expiredDate;

    public ProductPermissionToken(String token, String appId, Date expiredDate) {
        this.token = token;
        this.appId = appId;
        this.expiredDate=expiredDate;
        this.isActive=true;
    }
}
