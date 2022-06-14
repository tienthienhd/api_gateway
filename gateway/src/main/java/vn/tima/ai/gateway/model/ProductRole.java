package vn.tima.ai.gateway.model;


import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "product_feature_role")
public class ProductRole {

    @Id
    @Column("id")
    protected Integer id;

    @Column("role")
    protected String roleId;

    @Column("service_id")
    protected String serviceId;

    @Column("feature_regex_path")
    protected String featurePathRegex;

    @Column("method")
    protected String method;

    @Column("description")
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
