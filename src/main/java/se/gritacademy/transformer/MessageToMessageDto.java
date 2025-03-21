package se.gritacademy.transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import se.gritacademy.dto.MessageDto;
import se.gritacademy.model.Message;
import se.gritacademy.service.MessageService;
import se.gritacademy.utils.Encode;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Component
public class MessageToMessageDto {
    @Value("${key1}")
    private  String key1;
    @Value("${key2}")
    private  String key2;

    private final Encode encode;

    public MessageToMessageDto(Encode encode) {
        this.encode = encode;
    }

    public MessageDto transform(Message message) throws Exception {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setSender(message.getSender().getEmail());
//        String decrypt = encode.decrypt(message.getMessage(),encode.getSecretKeySpec(),encode.getIvParameterSpec());
//        System.out.println(decrypt);
        messageDto.setMessage(message.getMessage());
//        messageDto.setMessage(decrypt);

        messageDto.setRecipient(message.getRecipient().getEmail());
        messageDto.setDate(message.getDate());
        return messageDto;

    }
}
