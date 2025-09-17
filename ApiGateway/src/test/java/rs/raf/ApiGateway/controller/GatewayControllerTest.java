package rs.raf.ApiGateway.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(GatewayController.class)
public class GatewayControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testHome() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Schedule Management API Gateway")
                .jsonPath("$.status").isEqualTo("running")
                .jsonPath("$.routes").exists();
    }

    @Test
    public void testHealth() {
        webTestClient.get()
                .uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP")
                .jsonPath("$.service").isEqualTo("api-gateway");
    }
}