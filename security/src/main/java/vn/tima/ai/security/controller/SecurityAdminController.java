package vn.tima.ai.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.SecurityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.tima.ai.security.service.SecurityAdminService;


@Controller
@RequestMapping("/admin")
public class SecurityAdminController {

    @Autowired
    protected SecurityAdminService securityAdminService;


    @PostMapping("/create-appid")
    public @ResponseBody
    Mono<ResponseEntity<SecurityResponse>> createAppId(@RequestParam(name = "appId") String appId,
                                                       @RequestParam(name = "appKey") String appKey,
                                                       @RequestParam(name = "permissionRoles") String permissionRoles,
                                                       @RequestParam(name = "tokenAcceptDay") Integer tokenAcceptDay) {
        Mono<SecurityResponse> response = securityAdminService.createAppId(appId, appKey, permissionRoles, tokenAcceptDay);
        return response.map(securityResponse -> new ResponseEntity<>(securityResponse, HttpStatus.OK));
    }

    @PostMapping("/create-token")
    public @ResponseBody
    Mono<ResponseEntity<SecurityResponse>> createToken(@RequestParam(name = "appId") String appId,
                                                       @RequestParam(name = "appKey") String appKey) {
        return securityAdminService.createToken(appId, appKey)
                .map(securityResponse -> new ResponseEntity<>(securityResponse, HttpStatus.OK));

    }

    //
    @PostMapping("/grant-permission")
    public @ResponseBody
    Mono<ResponseEntity<SecurityResponse>> grantPermission(@RequestParam(name = "appId") String appId,
                                                           @RequestParam(name = "permissionRoles") String newPermissionRoles) {
        return securityAdminService.grantPermission(appId, newPermissionRoles)
                .map(securityResponse -> new ResponseEntity<>(securityResponse, HttpStatus.OK));
    }

    @PostMapping("/block/{appId}")
    public @ResponseBody
    Mono<ResponseEntity<SecurityResponse>> blockAppId(@PathVariable String appId) {
        return securityAdminService.blockAppId(appId)
                .map(securityResponse -> new ResponseEntity<>(securityResponse, HttpStatus.OK));
    }

//    @GetMapping("/force/logout/all")
//    public ResponseEntity<SecurityResponse> truncateToken(){
//        return new ResponseEntity<>(securityAdminService.truncateAllToken(), HttpStatus.OK);
//    }
//
//    @PostMapping("/force/logout/{appId}")
//    public ResponseEntity<SecurityResponse> forceLogout(@PathVariable String appId){
//        return new ResponseEntity<>(securityAdminService.truncateTokenAppId(appId), HttpStatus.OK);
//    }
//
//
//
//
//
//    @PostMapping("/post-test")
//    public ResponseEntity<SecurityResponse> postTest(@RequestParam(name = "mess") String receiveMess){
//        SecurityResponse response=new SecurityResponse(HttpStatus.OK, receiveMess);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @GetMapping("/get-test")
//    public ResponseEntity<SecurityResponse> getTest(@RequestParam(name = "mess") String receiveMess){
//        SecurityResponse response=new SecurityResponse(HttpStatus.OK, receiveMess);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

//    @PostMapping("/role/add")
//    public String addRole(String roleId, String serviceId, String pathRegex, String description){
//        return null;
//    }
//
//    @DeleteMapping("/role/delete/{roleId}")
//    public String deleteRole(String roleId){
//        return null;
//    }
}
