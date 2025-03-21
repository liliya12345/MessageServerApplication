package se.gritacademy.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.dto.MessageDto;
import se.gritacademy.model.Message;
import se.gritacademy.model.UserInfo;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@RequestHeader("Authorization") String token) {
        try{
            logger.debug("Request to retrieve messages received");
            Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
            String username = claims.getSubject();
            List<MessageDto> getMessages = messageService.getMessages(username);
            logger.info("Messages retrieved successfully for user: {}", username);
            return ResponseEntity.ok(getMessages);
        }catch (Exception e){
            logger.error("Failed to retrieve messages: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestHeader("Authorization") String token,
                                              @RequestParam String recipient,
                                             @RequestParam String message) {
//        @Valid @RequestParam Message message, BindingResult bindingResult) {
//        System.out.println();
//            if (bindingResult.hasErrors()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError().getDefaultMessage());
//            }
        logger.debug("Request to send message received");
        try {
            Claims claims = jwtTokenUtils.parseJwtToken(token.replace("Bearer ", ""));
            String username = claims.getSubject();
            logger.debug("Sending message from {} to {}", username, message);
            messageService.save(username,recipient, message);
        } catch (Exception e) {

            logger.error("Failed to send message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
        return ResponseEntity.ok().body("The message successfully send");
    }



}
