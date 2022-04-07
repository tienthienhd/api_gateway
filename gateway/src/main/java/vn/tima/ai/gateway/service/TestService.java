package vn.tima.ai.gateway.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.tima.ai.gateway.service.TestService;
import vn.tima.ai.gateway.repository.sql.ProductRolesRepo;
import vn.tima.ai.gateway.model.ProductRole;

import java.util.List;

@Service
public class TestService {

    @Autowired
    ProductRolesRepo productrolesrepo;

//    public TestService(ProductRolesRepo productRolesRepo){
//        this.productrolesrepo = productRolesRepo;
//    }

    public String process(){
        List<ProductRole> roles = productrolesrepo.findAllSortASC();
        System.out.println("ROLES: " + roles);
        return "test query database 111111111111";
    }
}
