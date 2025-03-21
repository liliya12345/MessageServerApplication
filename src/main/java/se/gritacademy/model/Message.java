package se.gritacademy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.*;

@Table
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Message should not be empty")
    private String message;

    private Date date;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sender_id", nullable = false)
    private UserInfo sender;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserInfo recipient;


    public Message() {

    }


    public Message(Long id, String message, Date date, UserInfo sender, UserInfo recipient) {
        this.id = id;
        this.message = message;
        this.date = date;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public UserInfo getRecipient() {
        return recipient;
    }

    public void setRecipient(UserInfo recipient) {
        this.recipient = recipient;
    }
}

