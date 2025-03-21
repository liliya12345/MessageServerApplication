package se.gritacademy.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.gritacademy.service.MessageService;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.transform.Result;
import java.util.Base64;



@Component
public class Encode {
    @Value("${key1}")
    private  String key1;
    @Value("${key2}")
    private  String key2;


    public IvParameterSpec getIvParameterSpec() {
        byte[] iv = key2.getBytes(); // 16 байт
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    public SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = key1.getBytes(); // 16 байт
        if (keyBytes.length != 16) {
            throw new IllegalArgumentException("Invalid key length: must be 16 bytes");
        }
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        return secretKey;
    }

    public  String encrypt(String message, SecretKey secretKey, IvParameterSpec ivParameterSpec) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Кодируем в Base64 для удобства
    }
    public  String decrypt(String encryptedMessage, SecretKeySpec secretKey, IvParameterSpec ivParameterSpec) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec); // Указываем IV
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes);
    }
}
