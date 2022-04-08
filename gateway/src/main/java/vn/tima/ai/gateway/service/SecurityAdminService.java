package vn.tima.ai.gateway.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.tima.ai.gateway.model.ProductRole;
import vn.tima.ai.gateway.repository.sql.ProductPartnersRepo;
import vn.tima.ai.gateway.repository.sql.ProductPermissionTokensRepo;
import vn.tima.ai.gateway.repository.sql.ProductRolesRepo;

import java.util.List;

@Service
public class SecurityAdminService {

    @Autowired
    protected ProductPermissionTokensRepo productPermissionTokensRepo;

    @Autowired
    protected ProductPartnersRepo productPartnersRepo;

    @Autowired
    protected ProductRolesRepo productRolesRepo;

    public List<ProductRole> getAllRoles(){
        return productRolesRepo.findAllSortASC();
    }

    public ProductRole getRoleIdByPath(String pathRegex){
        return productRolesRepo.findByFeaturePathRegex(pathRegex);
    }

}
