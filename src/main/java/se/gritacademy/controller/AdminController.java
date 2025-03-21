package se.gritacademy.controller;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.dto.MessageDto;
import se.gritacademy.model.Message;
import se.gritacademy.model.Role;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.MessageRepository;
import se.gritacademy.repository.UserRepository;
import se.gritacademy.service.MessageService;
import se.gritacademy.service.UserService;
import se.gritacademy.utils.JwtTokenUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final MessageService messageService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String ALLOWED_DIRECTORY = "log/";
    public AdminController(UserService userService, MessageService messageService, JwtTokenUtils jwtTokenUtils) {
        this.userService = userService;
        this.messageService = messageService;
        this.jwtTokenUtils = jwtTokenUtils;
    }


    @GetMapping("/admin/users")
    public List<UserInfo> getAllUsers() {
        logger.info("Get all users");
        return userService.findAll();
    }


    @GetMapping("/admin/messages")
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        List<MessageDto> allMessages = messageService.getAllMessages();
        logger.info("Get all messages");
        return ResponseEntity.ok(allMessages);
    }

    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMessages(@RequestHeader("Authorization") String token, @RequestBody Map<String, Integer> requestBody) {
        Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
        Integer messageId = requestBody.get("messageId");
        String role = claims.get("role").toString();
        try {
            if (role.equals("admin")) {
                if (!messageService.getMessagesById(messageId)) {
                    logger.error("Attempt to delete non-existent message: {}", messageId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
                }
                messageService.deleteMessage(messageId);
                logger.info("Delete admin message {}", messageId);
                return ResponseEntity.ok("message deleted successfully");
            } else  {
                logger.warn("Unauthorized attempt to delete message {} by non-admin user", messageId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can delete messages");
            }
        } catch (Exception e) {
            logger.error("Failed to delete message {}: {}", messageId, e.getMessage());
            return ResponseEntity.badRequest().body("Failed to delete message"+ e.getMessage());
        }
    }


    @PostMapping("/admin/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> block(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> requestBody) {
        Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
        String email = String.valueOf(requestBody.get("email"));
        String role = claims.get("role").toString();
        try {
            if (role.equals("admin")) {
                Optional<UserInfo> user = userService.findByEmail(email);
                if (user.isPresent()) {
                    if (!user.get().getBlocked()) {
                        user.get().setBlocked(true);
                        userService.save(user.get());
                        logger.info("Admin  blocked user successfully: {}", email);
                        return ResponseEntity.ok("user blocked successfully");
                    } else {
                        user.get().setBlocked(false);
                        userService.save(user.get());
                        logger.info("Admin unblocked user successfully: {}", email);
                        return ResponseEntity.ok("user unblocked successfully");
                    }
                } else {
                    logger.warn("Attempt to block/unblock non-existent user: {}", email);
                }
            } else {
                logger.warn("Unauthorized attempt to delete user {} by non-admin user", email);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can delete user");
            }
        } catch (Exception e) {
            logger.error("Failed to delete user {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body("Failed to delete user");
        }
        return ResponseEntity.badRequest().body("Failed to delete user");
    }
    @GetMapping("/admin/log")
    public ResponseEntity<String> getFile(){

        Path path = Paths.get("logfile.txt");
        if (!isValidPath(path.toString())) {
            logger.error("Invalid file path");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file path");
        }
//        Path path = Paths.get("logfile.txt");
//        Path path = Paths.get("logfile.txt").normalize();
        // Check if the file exists

        if (!Files.exists(path)) {
            logger.error("Log file not found: {}", path.toAbsolutePath());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        try {
            // Read the file into a byte array
            byte[] image = Files.readAllBytes(path);
            logger.info("Log file retrieved successfully: {}", path.toAbsolutePath());
            return ResponseEntity.ok(new String(image));

        } catch (IOException e) {
            logger.error("Failed to read log file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }
    public static boolean isValidPath(String path) {

        Path normalizedPath = Paths.get(path).normalize();

        // Kontrollera att sökvägen börjar med den tillåtna katalogen
        return normalizedPath.startsWith(ALLOWED_DIRECTORY);
    }


}


