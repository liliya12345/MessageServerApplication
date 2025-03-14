package se.gritacademy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.*;
import se.gritacademy.model.Message;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;

import java.util.*;
import java.util.Optional;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@SpringBootApplication
public class MessageServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageServerApplication.class, args);
    }
}

