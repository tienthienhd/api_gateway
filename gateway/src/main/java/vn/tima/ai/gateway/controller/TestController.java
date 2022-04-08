package vn.tima.ai.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tima.ai.gateway.model.ProductRole;
import vn.tima.ai.gateway.repository.sql.ProductRolesRepo;

import java.util.Arrays;
import java.util.List;
import vn.tima.ai.gateway.utils.JwtUtil;


@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    ProductRolesRepo productRolesRepo;

    @Autowired
    public JwtUtil jwtUtil;

    @RequestMapping("/get_roles")
    public List<ProductRole> test(){

        List<ProductRole> roles =  productRolesRepo.findAllSortASC();
        System.out.println("ROLES: " + roles);

        return roles;
    }

    @RequestMapping("/gen_token")
    public String genToken(){
        List authorities = Arrays.asList("amazing_crawler","appraisal_auto_check","auto_moj_secured_transfer");
        String token = jwtUtil.generateToken("ai_super_admin_test", authorities,86400000000L);
        return token;
    }

    @RequestMapping("/gen_token_public")
    public String genTokenPublic(){
        List authorities = Arrays.asList("PUBLIC");
        String token = jwtUtil.generateToken("ai_super_admin_test", authorities,86400000000L);
        return token;
    }

    @RequestMapping("/gen_token_expried")
    public String genTokenExpired(){
        List authorities = Arrays.asList("PUBLIC");
        String token = jwtUtil.generateToken("ai_super_admin_test", authorities,1000L);
        return token;
    }

}
