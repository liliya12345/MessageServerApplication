package se.gritacademy.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.model.Message;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;
import se.gritacademy.service.UserService;
import se.gritacademy.utils.JwtTokenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password) {
        if (userService.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        userService.save(new UserInfo(email, password, "user"));
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
    //del1...5
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(@RequestHeader("Authorization") String token) {
        Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));


        return ResponseEntity.ok(null);
    }
    //del1
    @GetMapping("/users")
    public ResponseEntity<List<String>> getUsers(@RequestHeader("Authorization") String token) {
        Claims bearer = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
        String role = bearer.get("role").toString();
        if(role.equals("admin")) {
            return ResponseEntity.ok(userService.findEmailUsers());
        }

        return ResponseEntity.ok(null);
    }
    //del1
    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestHeader("Authorization") String token, @RequestParam String recipient, @RequestParam String message) {
        Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
        ArrayList<String> messagesList = new ArrayList<>();




        return ResponseEntity.ok("Not implemented yet");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

}
