package se.gritacademy.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    public AuthController() {
    }

    public AuthController(UserRepository userRepository, JwtTokenUtils jwtTokenUtils, UserService userService) {
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
//            @RequestParam String email, @RequestParam String password) {
            @Valid UserInfo userInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bindingResult.getFieldError().getDefaultMessage());
        }

        if (userService.findByEmail(userInfo.getEmail()).isPresent()) {
            logger.warn("Registration attempt with existing email: {}", userInfo.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        Set<Role> roleSet =new HashSet<>();
        roleSet.add(Role.ROLE_USER);
        userService.save(new UserInfo(null,userInfo.getEmail(), userInfo.getPassword(), roleSet,false));
        logger.info("User registered successfully with email: {}", userInfo.getEmail());
        logger.debug("Registration request received for email: {}", userInfo.getEmail());

        //Validation
//        logger.error("Registration request received for email: {}", email);
//        logger.error(password);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
//            @Valid @RequestParam String email,
//            @Valid @RequestParam String password) {
            @Valid UserInfo userInfo, BindingResult bindingResult) {
  if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bindingResult.getFieldError().getDefaultMessage());
  }

        Optional<UserInfo> userOpt = userService.findByEmail(userInfo.getEmail());
        if (userOpt.isPresent()) {
            UserInfo user = userOpt.get();
            if(user.getBlocked()){
                logger.warn("Blocked user {} attempted to login", userInfo.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User blocked");
            }
                String token = jwtTokenUtils.generateJwtToken(user);
            logger.info("Generated token for user {}: {}", userInfo.getEmail(),token);
            logger.debug("User {} successfully logged in", userInfo.getEmail());
                return ResponseEntity.ok(token);
//            }
        }

        //Validation
        //        logger.error("Registration request received for email: {}", email);
//        logger.error(password);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }



    @GetMapping("/logout")
//    public ResponseEntity<String> logout() {
//        logger.info();
//        return ResponseEntity.ok("Logged out successfully");
//    }
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
        String email = claims.getSubject();
        String ipAddress = request.getRemoteAddr(); // Получаем IP-адрес пользователя

        // Логирование
        logger.info("User with email {} is  successfully logging out from IP address {}", email, ipAddress);

        // Логика выхода из системы
        return ResponseEntity.ok("Logged out successfully");
    }


}
