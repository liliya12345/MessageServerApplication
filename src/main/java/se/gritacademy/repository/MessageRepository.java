package se.gritacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.gritacademy.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findAllById(int messageId);
}
