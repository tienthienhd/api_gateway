package vn.tima.ai.security.service;



import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.SecurityResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.tima.ai.security.repository.sql.ProductPartnersRepo;
import vn.tima.ai.security.repository.sql.ProductPermissionTokensRepo;
import vn.tima.ai.security.repository.sql.ProductRolesRepo;
import vn.tima.ai.security.model.ProductPartner;
import vn.tima.ai.security.model.ProductPermissionToken;


import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

@Service
@Log4j2
public class SecurityAdminService {

    @Autowired
    protected ProductPermissionTokensRepo productPermissionTokensRepo;

    @Autowired
    protected ProductPartnersRepo productPartnersRepo;

    @Autowired
    protected ProductRolesRepo productRolesRepo;

//    @Autowired
//    private BCryptPasswordEncoder encoder;

    private boolean isDebug;

    private static final String ERROR_MESS="Can't do action";

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public SecurityResponse createAppId(String appId, String appKey, String permissionRoles, Integer tokenAcceptDay){

        try {
            ProductPartner partner= new ProductPartner(appId, encoder.encode(appKey), permissionRoles, tokenAcceptDay);
            productPartnersRepo.save(partner);
            return SecurityResponse.success();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new SecurityResponse(HttpStatus.FORBIDDEN, ERROR_MESS);
    }

    public SecurityResponse createToken(String appId, String appKey) {
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        ProductPartner productPartner = productPartnersRepo.findPartnerActiveByAppId(appId);
        if (productPartner != null){
            if (encoder.matches(appKey, productPartner.getAppKey())){
                log.info(productPartner.getPermissionRoles());
                log.info(productPartner.getTokenAcceptDay());
                List author = Arrays.asList("ROLE_"+productPartner.getPermissionRoles());
                Integer acceptDay = productPartner.getTokenAcceptDay();
                long now = System.currentTimeMillis();
                Date issuedAt = new Date(now);
                Date expiredDate;

                if (acceptDay != null) {
                    expiredDate = new Date(now + (86400000L * acceptDay));
                } else {
                    expiredDate = new Date(now + 86400000L);
                }
                String jwtToken = Jwts.builder()
                        .setSubject(appId)
                        .claim("authorities", author)
                        .setIssuedAt(issuedAt)
                        .setExpiration(expiredDate)
                        .signWith(hmacKey)
                        .compact();
//                public void saveToken(String appId, String token, Date expiredDate)
                saveToken(appId, jwtToken, expiredDate);

                SecurityResponse response = new SecurityResponse(HttpStatus.OK, "Bearer " + jwtToken);
                return response;
            }
        }

        SecurityResponse response = new SecurityResponse(HttpStatus.OK, "None");
        return response;
    }

    public SecurityResponse grantPermission(String appId, String permissionRole){
        try{
            productPartnersRepo.updatePermission(appId, permissionRole);
            return SecurityResponse.success();
        } catch (Exception e) {
            if(isDebug)
                e.printStackTrace();
        }
        return new SecurityResponse(HttpStatus.FORBIDDEN, ERROR_MESS);
    }

    public SecurityResponse blockAppId(String appId){

        try {
            productPermissionTokensRepo.deleteTokenByAppId(appId);
            productPartnersRepo.blockAppId(appId);
            return SecurityResponse.success();
        } catch (Exception e){
            if(isDebug)
                e.printStackTrace();
        }
        return new SecurityResponse(HttpStatus.FORBIDDEN, ERROR_MESS);
    }

    public void saveToken(String appId, String token, Date expiredDate) {
        log.info(appId, token, expiredDate);
        productPermissionTokensRepo.save(new ProductPermissionToken(token, appId, expiredDate));
    }

}
