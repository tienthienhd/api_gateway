package vn.tima.ai.security.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.tima.ai.security.repository.sql.ProductPartnersRepo;
import vn.tima.ai.security.repository.sql.ProductPermissionTokensRepo;
import vn.tima.ai.security.repository.sql.ProductRolesRepo;


import java.util.List;

@Service
public class SecurityAdminService {

    @Autowired
    protected ProductPermissionTokensRepo productPermissionTokensRepo;

    @Autowired
    protected ProductPartnersRepo productPartnersRepo;

    @Autowired
    protected ProductRolesRepo productRolesRepo;

}
