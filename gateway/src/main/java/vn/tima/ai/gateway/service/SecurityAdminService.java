package vn.tima.ai.gateway.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.tima.ai.gateway.model.ProductRole;
import vn.tima.ai.gateway.repository.sql.ProductPartnersRepo;
import vn.tima.ai.gateway.repository.sql.ProductPermissionTokensRepo;
import vn.tima.ai.gateway.repository.sql.ProductRolesRepo;

@Service
public class SecurityAdminService {

    @Autowired
    protected ProductPermissionTokensRepo productPermissionTokensRepo;

    @Autowired
    protected ProductPartnersRepo productPartnersRepo;

    @Autowired
    protected ProductRolesRepo productRolesRepo;

    public Flux<ProductRole> getAllRoles(){
        return productRolesRepo.findAllSortASC();
    }

    public Mono<ProductRole> getRoleIdByPath(String pathRegex){
        return productRolesRepo.findByFeaturePathRegex(pathRegex);
    }

}
