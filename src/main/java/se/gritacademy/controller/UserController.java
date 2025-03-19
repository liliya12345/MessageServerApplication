package se.gritacademy.controller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.service.UserService;
import se.gritacademy.utils.JwtTokenUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserService userService;



    @GetMapping("/users")
    public ResponseEntity<List<String>> getUsers(@RequestHeader("Authorization") String token) {
        Claims bearer = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
        String role = bearer.get("role").toString();
//        if(role.equals("admin")) {
//            return ResponseEntity.ok(userService.findEmailUsers());
//        }

        return ResponseEntity.ok(userService.findEmailUsers());
    }

}
