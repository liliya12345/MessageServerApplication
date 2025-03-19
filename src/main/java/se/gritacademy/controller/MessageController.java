package se.gritacademy.controller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.dto.MessageDto;
import se.gritacademy.model.Message;
import se.gritacademy.service.MessageService;
import se.gritacademy.utils.JwtTokenUtils;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MessageController {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@RequestHeader("Authorization") String token) {
        Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
        String username = claims.getSubject();
        List<MessageDto> getMessages = messageService.getMessages(username);
        return ResponseEntity.ok(getMessages);
    }
    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestHeader("Authorization") String token, @RequestParam String recipient, @RequestParam String message) {
        Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
       String username= claims.getSubject();
       messageService.save(username,recipient,message);
        return ResponseEntity.ok("Not implemented yet");
    }



}
