package se.gritacademy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import se.gritacademy.model.Role;
import se.gritacademy.model.UserInfo;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    @Value("QWQKEOKOKOKOQEQe565vv565vvvv5656566bjhjhjhjhjjjcxszszszszszszszszszszszsz")
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
        String role= "";
        if(roleList.get(0).equals("ROLE_ADMIN")) {
            role = "admin";
        }
        else if(roleList.get(0).equals("ROLE_USER")) {
            role = "user";
        }
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(36000000)))
//                .setClaims(claims)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }


//    public Claims parseJwtToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token) // Use parseClaimsJws for signed JWTs
//                .getBody();
//    }
    public List<String> getRole(String token) {
//        return parseJwtToken(token).get("role", List.class);
        List<String>roleList=new ArrayList<>();
        String role = parseJwtToken(token).get("role").toString();
      if(role.equals("admin")) {
          roleList.add(Role.ROLE_ADMIN.name());
      }
      else if(role.equals("user")) {
          roleList.add(Role.ROLE_USER.name());
      }
//        roleList.add(Role.ROLE_USER.name());
        return roleList;
    }

    public String getUserName(String jwt) {
        return parseJwtToken(jwt).getSubject();
    }

//    public String generateJwtToken(UserInfo user) {
//        Map<String, Object> claims = new HashMap<>();
//        List<String> roleList = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
//        claims.put("roles", roleList);
//        String role= "";
//        if(roleList.get(0).equals("ROLE_ADMIN")) {
//            role = "admin";
//        }
//        else if(roleList.get(0).equals("ROLE_USER")) {
//            role = "user";
//        }
//        return Jwts.builder()
//                .setSubject(user.getEmail())
//                .claim("role", role)
//                .signWith(SignatureAlgorithm.HS256,secretKey)
//                .compact();
//    }

    public Claims parseJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }







}
