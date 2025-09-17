package rs.raf.RasporedService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get("role");
    }

    public Integer getUserIdFromToken(String token) {
        return (Integer) getAllClaimsFromToken(token).get("id");
    }

    public String getEmailFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get("email");
    }

    public boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}