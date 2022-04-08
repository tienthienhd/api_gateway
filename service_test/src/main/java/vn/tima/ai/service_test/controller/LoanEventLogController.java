package vn.tima.ai.service_test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/loan-event-log")
public class LoanEventLogController {

    @GetMapping("/2")
    public String test() {
        return "test service loan-event-log";
    }
}