package se.gritacademy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
public class UserInfo implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email should be meet the requirements")
    @NotEmpty(message = "Email should not be empty")
    @Size(min = 6, max=50 , message = "Email should be between 6 to 50 characters")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 15, max=100 , message = "Password should be between 15 to 100 characters")
    @Pattern(message = "the Password must contain at least one capital letter,at least 1 uppercase letter\n" +
            "at least 1 lowercase letter\n" +
            "at least 1 number\n" +
            "at least 1 special character",regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{12,}$")
    private String password;
    private Boolean blocked;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> sendMessageList;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> recivedMessageList;


    public UserInfo(Long id, String email, String password, Set<Role> userRoles,Boolean blocked) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
        this.blocked = blocked;
    }


    public UserInfo()  {}

    public UserInfo(Long id, String email, String password, Set<Role> userRoles, List<Message> sendMessageList, List<Message> recivedMessageList) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
        this.sendMessageList = sendMessageList;
        this.recivedMessageList = recivedMessageList;
    }


    public String getEmail() { return email; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  userRoles;
    }

    public String getPassword() { return password; }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public List<Message> getSendMessageList() {
        return sendMessageList;
    }

    public void setSendMessageList(List<Message> sendMessageList) {
        this.sendMessageList = sendMessageList;
    }

    public List<Message> getRecivedMessageList() {
        return recivedMessageList;
    }

    public void setRecivedMessageList(List<Message> recivedMessageList) {
        this.recivedMessageList = recivedMessageList;
    }

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }
}
