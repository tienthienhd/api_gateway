package vn.tima.ai.service_test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appraisal-auto-check")
public class DemoController {

    @GetMapping("/1")
    public String test1() {
        return "test service1 appraisal_auto_check_1111111111";
    }

    @GetMapping("/2")
    public String test2() {
        return "test service1 appraisal_auto_check_2222222222";
    }
}