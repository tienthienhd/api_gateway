package vn.tima.ai.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tima.ai.gateway.service.TestService;

@RestController
@RequestMapping("/data")
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/test")
    public String test() {
        String result = testService.process();
        return result;
    }

}
