package rs.raf.AuthService.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    @Value("${oauth.jwt.expiration:86400000}") // Default: 24 hours in milliseconds
    private long jwtExpiration;

    public String generate(Claims claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setIssuer("schedule-management-auth-service")
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateWithCustomExpiry(Claims claims, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setIssuer("schedule-management-auth-service")
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Claims parseToken(String jwt) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt)
                    .getBody();
            
            // Check if token is expired
            if (claims.getExpiration().before(new Date())) {
                return null;
            }
            
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValid(String jwt) {
        return parseToken(jwt) != null;
    }

    public boolean isTokenExpired(String jwt) {
        Claims claims = parseToken(jwt);
        return claims != null && claims.getExpiration().before(new Date());
    }

}
