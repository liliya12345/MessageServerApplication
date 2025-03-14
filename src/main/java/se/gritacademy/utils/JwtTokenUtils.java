package se.gritacademy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import se.gritacademy.model.UserInfo;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    @Value("QWQKEOKOKOKOQEQe565vv565vvvv5656566bjhjhjhjhjjjcxszszszszszszszszszszszszszs")
    private String secretKey;

    public JwtTokenUtils() {
    }

    public JwtTokenUtils(String secretKey) {
        this.secretKey = secretKey;
    }


    public String generateJwtToken(UserInfo user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        claims.put("roles", roleList);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)))
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public Claims parseJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token) // Use parseClaimsJws for signed JWTs
                .getBody();
    }
    public List<String> getRole(String token) {
        return parseJwtToken(token).get("role", List.class);
    }

    public String getUserName(String jwt) {
        return parseJwtToken(jwt).getSubject();
    }
}
