package vn.tima.ai.gateway.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.tima.ai.gateway.model.LogRequests;
import vn.tima.ai.gateway.model.ProductRole;

import java.util.List;

public interface LogRequestsRepo extends JpaRepository<LogRequests, String> {

    @Query("select lr from LogRequests lr where lr.request_id = :request_id")
    LogRequests findByRequest_id(@Param("request_id") String pathRegex);

}
