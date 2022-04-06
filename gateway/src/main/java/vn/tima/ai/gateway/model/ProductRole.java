package vn.tima.ai.gateway.model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_feature_role_v2")
@ToString
public class ProductRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;
    @Column(name = "role")
    protected String roleId;
    @Column(name = "service_id")
    protected String serviceId;
    @Column(name = "feature_regex_path")
    protected String featurePathRegex;
    @Column(name = "method")
    protected String method;
    @Column(name = "description")
    protected String description;

    @Override
    public String toString() {
        return "Role: " +
                "roleId='" + roleId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", featurePathRegex='" + featurePathRegex + '\'';
    }

    public boolean isChildRoleOf(ProductRole role){
        return (role.method == null || role.method.equals(this.method)) && this.featurePathRegex.matches(role.getFeaturePathRegex().replaceAll("\\*\\*",".*"));
    }

    public enum FixRole{
        PUBLIC,
        SECURITY_ADMIN
    }
}
