package vn.tima.ai.gateway.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("log_request_api_gateway")
@ToString
public class LogRequest {
    @Id
    @Column("id")
    protected Integer id;

    @Column("request_id")
    protected String requestId;

    @Column("uri")
    protected String uri;

    @Column("method")
    protected String method;

    @Column("timestamp")
    protected String timestamp;

    @Column("duration")
    protected float duration;

    @Column("path")
    protected String path;

    @Column("app_id")
    protected String appId;

    @Column("host_address")
    protected String hostAddress;

    @Column("input_body")
    protected String inputBody;

    @Column("status_code")
    protected String statusCode;

    @Column("response")
    protected String response;

}
