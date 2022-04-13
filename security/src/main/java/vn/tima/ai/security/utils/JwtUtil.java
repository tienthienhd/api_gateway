package vn.tima.ai.security.utils;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.tima.ai.security.exception.JwtTokenMalformedException;
import vn.tima.ai.security.exception.JwtTokenMissingException;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret:7aW-RZ5Jb_V5p_CCC1r79sfWiSebxAZf4MILDJ9E9TIV2R9F_3aVvA2st85rf4C4PleM_EE-0B6Bu6xAQB0TEE8di7zsXv1I_KC2Yt2bz7dcnKAu63YlBVETklbsSLya7dKHfGzAcaA3Byka0sd5onVMDT9w85UUuN9tyf1zE7R_tbKt-Y30IW0aUoWkjn0uM9APbkMOUcvSg5Ym8JlwSFDrkFQFn6IKVoiOd0B5pMB15pX8kojWYP441pjfOmGYiuMDrrsIyUQ6xjQBhtn11lYtJo0QbrC89dVS-oVhqkDLUO12gQ7-jCx4WZs7n8uLuUt3glu0VHD8GqMlW5rEJw")
    private String jwtSecret="7aW-RZ5Jb_V5p_CCC1r79sfWiSebxAZf4MILDJ9E9TIV2R9F_3aVvA2st85rf4C4PleM_EE-0B6Bu6xAQB0TEE8di7zsXv1I_KC2Yt2bz7dcnKAu63YlBVETklbsSLya7dKHfGzAcaA3Byka0sd5onVMDT9w85UUuN9tyf1zE7R_tbKt-Y30IW0aUoWkjn0uM9APbkMOUcvSg5Ym8JlwSFDrkFQFn6IKVoiOd0B5pMB15pX8kojWYP441pjfOmGYiuMDrrsIyUQ6xjQBhtn11lYtJo0QbrC89dVS-oVhqkDLUO12gQ7-jCx4WZs7n8uLuUt3glu0VHD8GqMlW5rEJw";

    String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
    Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());

    private Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public Claims parseToken(String token) {
        Jwts.parserBuilder().setSigningKey(key).build();
        token = token.replaceFirst("Bearer ", "");
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims parseJwt(String jwtString) {
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        jwtString = jwtString.replaceFirst("Bearer ", "");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString)
                .getBody();

        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString)
                .getBody();
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

    public String generateToken(String appId, Collection<String> authorityRoles, Long acceptDay) {
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiredDate;
        if (acceptDay != null) {
            expiredDate = new Date(now + (86400000L * acceptDay));
        } else {
            expiredDate = new Date(now + 86400000L);
        }
        String jwtToken = Jwts.builder()
                .setSubject(appId)
                .claim("authorities", authorityRoles)
                .setIssuedAt(issuedAt)
                .setExpiration(expiredDate)
                .signWith(hmacKey)
                .compact();

        return jwtToken;

    }


    public Claims validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
//            parseToken(token);
            return parseJwt(token);
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