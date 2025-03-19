package se.gritacademy.transformer;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import se.gritacademy.dto.MessageDto;
import se.gritacademy.model.Message;
@Component
public class MessageToMessageDto {
    public MessageDto transform(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setSender(message.getSender().getEmail());
        messageDto.setMessage(message.getMessage());
        messageDto.setRecipient(message.getRecipient().getEmail());
        messageDto.setDate(message.getDate());
        return messageDto;

    }
}
