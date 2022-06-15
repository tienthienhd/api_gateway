package vn.tima.ai.security.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.SecurityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import vn.tima.ai.security.model.ProductPartner;
import vn.tima.ai.security.model.ProductPermissionToken;
import vn.tima.ai.security.repository.sql.ProductPartnersRepo;
import vn.tima.ai.security.repository.sql.ProductPermissionTokensRepo;
import vn.tima.ai.security.repository.sql.ProductRolesRepo;
import vn.tima.ai.security.utils.JwtUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
@Log4j2
public class SecurityAdminService {

    @Autowired
    protected ProductPermissionTokensRepo productPermissionTokensRepo;

    @Autowired
    protected ProductPartnersRepo productPartnersRepo;

    @Autowired
    protected ProductRolesRepo productRolesRepo;

    @Autowired
    protected JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private boolean isDebug;

    private static final String ERROR_MESS = "Can't do action";

    @Transactional
    public Mono<SecurityResponse> createAppId(String appId, String appKey, String permissionRoles, Integer tokenAcceptDay) {
        return productPartnersRepo.findByAppId(appId).log()
                .flatMap(foundPartner -> {
                    foundPartner.setAppKey(encoder.encode(appKey));
                    foundPartner.setPermissionRoles(permissionRoles);
                    foundPartner.setTokenAcceptDay(tokenAcceptDay);
                    return productPartnersRepo.save(foundPartner);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    ProductPartner partner = new ProductPartner(appId, encoder.encode(appKey), permissionRoles, tokenAcceptDay);
                    return productPartnersRepo.save(partner);
                }))
                .map(productPartner1 -> SecurityResponse.success())
                .switchIfEmpty(Mono.just(new SecurityResponse(HttpStatus.FORBIDDEN, ERROR_MESS)));
    }


    public Mono<SecurityResponse> createToken(String appId, String appKey) {

        return productPartnersRepo.findByAppIdAndBlockIsFalse(appId).log()
                .map(productPartner -> {
                    if (encoder.matches(appKey, productPartner.getAppKey())) {
                        List author = Collections.singletonList(productPartner.getPermissionRoles());
                        Integer acceptDay = productPartner.getTokenAcceptDay();

                        String jwtToken = jwtUtil.generateToken(appId, author, (float) acceptDay);
                        saveToken(appId, jwtToken);
                        return new SecurityResponse(HttpStatus.OK, JwtUtil.TOKEN_PREFIX + jwtToken);
                    } else {
                        return new SecurityResponse(HttpStatus.OK, "None");
                    }
                }).switchIfEmpty(Mono.just(new SecurityResponse(HttpStatus.OK, "None")));
    }

    public Mono<SecurityResponse> grantPermission(String appId, String permissionRole) {
        return productPartnersRepo.findByAppId(appId)
                .flatMap(productPartner -> {
                    productPartner.setPermissionRoles(permissionRole);
                    return productPartnersRepo.save(productPartner)
                            .map(productPartner1 -> SecurityResponse.success());
                }).switchIfEmpty(Mono.just(new SecurityResponse(HttpStatus.FORBIDDEN, ERROR_MESS)));

    }

    public Mono<SecurityResponse> blockAppId(String appId) {
        return productPermissionTokensRepo.deleteByAppId(appId)
                .flatMap(numDelete -> {
                    log.info("Delete {} tokens of {}", numDelete, appId);
                    return productPartnersRepo.findByAppId(appId);
                }).flatMap(productPartner -> {
                    productPartner.setBlock(true);
                    return productPartnersRepo.save(productPartner);
                }).map(productPartner -> SecurityResponse.success())
                .switchIfEmpty(Mono.just(new SecurityResponse(HttpStatus.FORBIDDEN, ERROR_MESS)));
    }

    public void saveToken(String appId, String token, Date expiredDate) {
        log.info(appId, token, expiredDate);
        productPermissionTokensRepo.save(new ProductPermissionToken(token, appId, expiredDate));
    }

    public void saveToken(String appId, String token) {
        this.saveToken(appId, token, null);
    }

}
