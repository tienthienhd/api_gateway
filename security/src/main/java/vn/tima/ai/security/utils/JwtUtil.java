package vn.tima.ai.security.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.tima.ai.security.exception.JwtTokenMalformedException;
import vn.tima.ai.security.exception.JwtTokenMissingException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret_key}")
    private String jwtSecret;

    public Claims parseToken(String jwtString) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
                SignatureAlgorithm.HS256.getJcaName());
        jwtString = jwtString.replaceFirst(TOKEN_PREFIX, "");

        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString)
                .getBody();
    }

    public String generateToken(String appId, Collection<String> authorityRoles, Float acceptDay) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiredDate;
        if (acceptDay != null) {
            expiredDate = new Date((long) (now + (86400000L * acceptDay))); // 86400000L = 1 day
        } else {
            expiredDate = new Date(now + 86400000L);
        }

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
                SignatureAlgorithm.HS256.getJcaName());

        return TOKEN_PREFIX + Jwts.builder()
                .setSubject(appId)
                .claim("authorities", authorityRoles)
                .setIssuedAt(issuedAt)
                .setHeaderParam("typ", "JWT")
                .setExpiration(expiredDate)
                .signWith(hmacKey).compact();

    }


    public Claims validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            return parseToken(token);
        } catch (SecurityException ex) {
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