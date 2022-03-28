package vn.tima.ai.gateway.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @RequestMapping("/first")
    public Mono<String> fallback() {
        return Mono.just("This is a fallback for first service.");
    }
}
