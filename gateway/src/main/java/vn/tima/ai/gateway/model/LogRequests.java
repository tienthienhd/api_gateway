package vn.tima.ai.gateway.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "log_request_api_gateway")
@ToString
public class LogRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @Column(name = "request_id")
    protected String request_id;

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
    protected String app_id;

    @Column(name = "host_address")
    protected String host_address;

    @Column(name = "input_body")
    protected String input_body;

    @Column(name = "status_code")
    protected String status_code;

    @Column(name = "response")
    protected String response;

}
