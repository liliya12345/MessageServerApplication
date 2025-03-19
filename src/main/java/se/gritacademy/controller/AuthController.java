package se.gritacademy.controller;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.model.Role;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;
import se.gritacademy.service.UserService;
import se.gritacademy.utils.JwtTokenUtils;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserService userService;

    public AuthController() {
    }

    public AuthController(UserRepository userRepository, JwtTokenUtils jwtTokenUtils, UserService userService) {
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password) {
        if (userService.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        Set<Role> roleSet =new HashSet<>();
        roleSet.add(Role.ROLE_USER);
        userService.save(new UserInfo(null,email, password, roleSet));
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        Optional<UserInfo> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            UserInfo user = userOpt.get();
//            String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());
            boolean checkpw = BCrypt.checkpw(password, user.getPassword());
            if (checkpw) {
                String token = jwtTokenUtils.generateJwtToken(user);
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }



    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

}
