package vn.tima.ai.gateway.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.tima.ai.gateway.exception.JwtTokenMalformedException;
import vn.tima.ai.gateway.exception.JwtTokenMissingException;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:VVmnwNb5FSiSN4s2jPzQYloe6dExOnRX}")
    private String jwtSecret;


    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    public String generateToken(String headToken, String appId, Collection<String> authorityRoles, Long acceptDay) {

        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiredDate;
        if (acceptDay != null) {
            expiredDate = new Date(now + (86400000L * acceptDay));
        } else {
            expiredDate = new Date(now + 86400000L);
        }
        return Jwts.builder()
                .setSubject(appId)
                .claim("authorities", authorityRoles)
                .setIssuedAt(issuedAt)
                .setExpiration(expiredDate)
                .signWith(key).compact();
    }

    public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            parseToken(token);
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }
}