package se.gritacademy.controller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.dto.MessageDto;
import se.gritacademy.model.Message;
import se.gritacademy.model.Role;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;
import se.gritacademy.service.MessageService;
import se.gritacademy.service.UserService;
import se.gritacademy.utils.JwtTokenUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final MessageService messageService;

    public AdminController(UserService userService, MessageService messageService, JwtTokenUtils jwtTokenUtils) {
        this.userService = userService;
        this.messageService = messageService;
        this.jwtTokenUtils = jwtTokenUtils;
    }


    @GetMapping("/admin/users")
    public List<UserInfo> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/admin/messages")
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        List<MessageDto> allMessages = messageService.getAllMessages();
        return ResponseEntity.ok(allMessages);
    }

    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMessages(@RequestHeader("Authorization") String token, @RequestBody Map<String, Integer> requestBody) {
        Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
        Integer messageId = requestBody.get("messageId");
        String role = claims.get("role").toString();
        if (role.equals("admin")) {
            messageService.deleteMessage(messageId);
            return ResponseEntity.ok("message deleted successfully");
        }
        return ResponseEntity.badRequest().body("invalid message");
    }

}


