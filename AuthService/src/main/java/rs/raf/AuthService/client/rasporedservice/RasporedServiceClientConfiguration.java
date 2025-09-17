package rs.raf.AuthService.client.rasporedservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import rs.raf.AuthService.security.service.TokenService;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class RasporedServiceClientConfiguration {

    @Autowired
    private TokenService tokenService;

    @Bean
    public RestTemplate rasporedServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8080/api/raspored"));
        restTemplate.setInterceptors(Collections.singletonList(new TokenInterceptor()));
        return restTemplate;
    }

    private class TokenInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                            ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            HttpHeaders headers = httpRequest.getHeaders();
            
            // Generate a service token for inter-service communication
            Claims claims = Jwts.claims();
            claims.put("service", "auth-service");
            claims.put("role", "ROLE_ADMIN");
            claims.put("id", -1); // Service account ID
            claims.setSubject("auth-service");
            
            String serviceToken = tokenService.generateWithCustomExpiry(claims, 300000); // 5 minutes
            headers.add("Authorization", "Bearer " + serviceToken);
            
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        }
    }
}
