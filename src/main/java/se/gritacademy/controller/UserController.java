package se.gritacademy.controller;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);



    @GetMapping("/users")
    public ResponseEntity<List<String>> getUsers(@RequestHeader("Authorization") String token) {
        try{
            logger.debug("Request to retrieve users received");
            Claims claims= jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
            String user = claims.getSubject();
            List<String> users = userService.findEmailUsers();
            logger.info("Users retrieved successfully by user: {}", user);
            return ResponseEntity.ok(users);
        }catch (Exception e) {
            // Логирование ошибки
            logger.error("Failed to retrieve users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }


}
