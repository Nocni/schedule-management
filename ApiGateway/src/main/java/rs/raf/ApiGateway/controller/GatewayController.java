package rs.raf.ApiGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GatewayController {

    @GetMapping("/")
    public Mono<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Schedule Management API Gateway");
        response.put("version", "1.0.0");
        response.put("status", "running");
        
        Map<String, String> routes = new HashMap<>();
        routes.put("auth", "/api/auth/** -> http://localhost:8081");
        routes.put("raspored", "/api/raspored/** -> http://localhost:8082");
        routes.put("notification", "/api/notification/** -> http://localhost:8083");
        
        response.put("routes", routes);
        return Mono.just(response);
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "api-gateway");
        return Mono.just(response);
    }
}