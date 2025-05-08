package limecoding.asmrstreamingservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.access.expiredMs}")
    private Duration accessTokenExpireMs;

    @Value("${jwt.token.refresh.expiredMs}")
    private Duration refreshTokenExpireMs;

    public String createAccessToken(String userId) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpireMs.toMillis()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String userId) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpireMs.toMillis()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getUserId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
