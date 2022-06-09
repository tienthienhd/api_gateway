package vn.tima.ai.gateway.repository.sql;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import vn.tima.ai.gateway.model.LogRequest;

public interface LogRequestRepo extends ReactiveCrudRepository<LogRequest, Integer> {

    Flux<LogRequest> findByRequestId(String requestId);
}
