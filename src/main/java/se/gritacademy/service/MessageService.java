package se.gritacademy.service;

import org.springframework.stereotype.Service;
import se.gritacademy.dto.MessageDto;
import se.gritacademy.model.Message;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.MessageRepository;
import se.gritacademy.repository.UserRepository;
import se.gritacademy.transformer.MessageToMessageDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageToMessageDto messageToMessageDto;


    public MessageService(UserRepository userRepository, MessageRepository messageRepository, MessageToMessageDto messageToMessageDto) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.messageToMessageDto = messageToMessageDto;
    }

    public void save(String username, String recipient, String message) {
        Optional<UserInfo> sender = userRepository.findByEmail(username);
        Optional<UserInfo> recipientUser = userRepository.findByEmail(recipient);
        if (sender.isPresent() && recipientUser.isPresent()) {
            Message message1 = new Message();
            message1.setSender(sender.get());
            message1.setMessage(message);
            message1.setRecipient(recipientUser.get());
            message1.setDate(new Date());
            messageRepository.save(message1);
        }




    }

    public List<MessageDto> getMessages(String username) {
        Optional<UserInfo> sender = userRepository.findByEmail(username);
       if (sender.isPresent()) {
           return sender.get().getSendMessageList().stream().map(s -> messageToMessageDto.transform(s)).toList();
       }
       return null;
    }

    public List<MessageDto> getAllMessages() {
        List<Message> all = messageRepository.findAll();
        List<MessageDto> list = all.stream().map(messageToMessageDto::transform).toList();
        return list;
    }
    public void deleteMessage(int messageId) {
        Message allById = messageRepository.findAllById(messageId);
        messageRepository.delete(allById);

    }

}
