package vn.tima.ai.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.tima.ai.security.service.SecurityAdminService;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.SecurityResponse;


@Controller
@RequestMapping("/admin")
public class SecurityAdminController {

    @Autowired
    protected SecurityAdminService securityAdminService;


    @PostMapping("/create-appid")
    public @ResponseBody
    ResponseEntity<SecurityResponse> createAppId(@RequestParam(name = "appId") String appId,
                                                 @RequestParam(name = "appKey") String appKey,
                                                 @RequestParam(name = "permissionRoles") String permissionRoles,
                                                 @RequestParam(name = "tokenAcceptDay") Integer tokenAcceptDay){
        SecurityResponse response= securityAdminService.createAppId(appId, appKey, permissionRoles, tokenAcceptDay);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create-token")
    public @ResponseBody
    ResponseEntity<SecurityResponse> createToken(@RequestParam(name = "appId") String appId,
                                                 @RequestParam(name = "appKey") String appKey){
        SecurityResponse response= securityAdminService.createToken(appId, appKey);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
//
    @PostMapping("/grant-permission")
    public @ResponseBody
    ResponseEntity<SecurityResponse> grantPermission(@RequestParam(name = "appId") String appId, @RequestParam(name = "permissionRoles") String newPermissionRoles){
        SecurityResponse response=securityAdminService.grantPermission(appId, newPermissionRoles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/block/{appId}")
    public ResponseEntity<SecurityResponse> blockAppId(@PathVariable String appId){
        SecurityResponse response=securityAdminService.blockAppId(appId);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
