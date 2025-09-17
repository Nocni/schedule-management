package rs.raf.ApiGateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.raf.ApiGateway.filter.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authFilter;

    public GatewayConfig(AuthenticationFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Public Auth routes (login, register)
                .route("auth-public", r -> r
                        .path("/api/auth/auth/prijava", "/api/auth/user/register")
                        .uri("http://localhost:8081"))

                // Protected Auth routes
                .route("auth-protected", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:8081"))

                // Protected Schedule routes
                .route("raspored-protected", r -> r
                        .path("/api/raspored/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:8082"))

                // Protected Notification routes
                .route("notification-protected", r -> r
                        .path("/api/notification/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:8083"))

                // Health check route
                .route("health", r -> r
                        .path("/health")
                        .filters(f -> f.setStatus(200))
                        .uri("no://op"))

                .build();
    }
}