package se.gritacademy.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.gritacademy.controller.AuthController;
import se.gritacademy.dto.MessageDto;
import se.gritacademy.model.Message;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.MessageRepository;
import se.gritacademy.repository.UserRepository;
import se.gritacademy.transformer.MessageToMessageDto;
import se.gritacademy.utils.Encode;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Service
public class MessageService {
    @Value("${key1}")
    private  String key1;
    @Value("${key2}")
    private  String key2;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageToMessageDto messageToMessageDto;
    private final Encode encode;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    public MessageService(UserRepository userRepository, MessageRepository messageRepository, MessageToMessageDto messageToMessageDto, Encode encode) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.messageToMessageDto = messageToMessageDto;
        this.encode = encode;
    }

    public void save(String username, String recipient, String message) {
        try{

            String encrypt = encode.encrypt(message, encode.getSecretKeySpec(), encode.getIvParameterSpec());
            Optional<UserInfo> sender = userRepository.findByEmail(username);
            Optional<UserInfo> recipientUser = userRepository.findByEmail(recipient);
            if (sender.isPresent() && recipientUser.isPresent()) {
                Message message1 = new Message();
                message1.setSender(sender.get());
                message1.setMessage(encrypt);
                message1.setRecipient(recipientUser.get());
                message1.setDate(new Date());
                messageRepository.save(message1);

                logger.info("Message saved to database");
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public List<MessageDto> getMessages(String username) {

       try {
           Optional<UserInfo> sender = userRepository.findByEmail(username);
           if (sender.isPresent()) {
               logger.info("Getting messages from database");

               return sender.get().getSendMessageList().stream().map(s -> {
                   try {
                       System.out.println(s.getMessage());;
                     String   decrypt = encode.decrypt(s.getMessage(), encode.getSecretKeySpec(), encode.getIvParameterSpec());
                       s.setMessage(decrypt);
                     return messageToMessageDto.transform(s);
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }
               }).toList();
           }}
       catch (Exception e) {
           logger.error(e.getMessage());
           return null;
       }
       return null;
    }



    public List<MessageDto> getAllMessages() {
        List<Message> all = messageRepository.findAll();
        try {
            List<MessageDto>list = all.stream().map(s -> {
                try {
                    return messageToMessageDto.transform(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).toList();
            return list;
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
    public void deleteMessage(int messageId) {
        Message allById = messageRepository.findAllById(messageId);
        messageRepository.delete(allById);
        logger.info("Message deleted from database");

    }

    public boolean getMessagesById(Integer messageId) {
        Message allById = messageRepository.findAllById(messageId);
        if (allById != null) {
            logger.info("Getting messages from database");
            return true;

        }
        logger.error("Message not found");
        return false;
    }


}
