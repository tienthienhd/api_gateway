package vn.tima.ai.gateway.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "log_request_api_gateway")
@ToString
public class LogRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Column(name = "request_id")
    protected String requestId;

    @Column(name = "uri")
    protected String uri;

    @Column(name = "method")
    protected String method;

    @Column(name = "timestamp")
    protected String timestamp;

    @Column(name = "duration")
    protected float duration;

    @Column(name = "path")
    protected String path;

    @Column(name = "app_id")
    protected String appId;

    @Column(name = "host_address")
    protected String hostAddress;

    @Column(name = "input_body")
    protected String inputBody;

    @Column(name = "status_code")
    protected String statusCode;

    @Column(name = "response")
    protected String response;

}
