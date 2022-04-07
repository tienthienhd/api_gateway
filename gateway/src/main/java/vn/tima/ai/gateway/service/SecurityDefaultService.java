package vn.tima.ai.gateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.tima.ai.gateway.model.ProductRole;
import vn.tima.ai.gateway.repository.sql.ProductRolesRepo;


@Service
public class SecurityDefaultService {

//    @Autowired
    public ProductRolesRepo productRolesRepo;

    public List<ProductRole> getAllRoles(){
        return productRolesRepo.findAllSortDESC();
    }
}
