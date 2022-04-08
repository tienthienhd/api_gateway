package vn.tima.ai.service_test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appraisal-auto-check")
public class DemoController {

    @GetMapping("/1")
    public String test() {
        return "test service appraisal_auto_check";
    }
}