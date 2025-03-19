package se.gritacademy.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
public class UserInfo implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> sendMessageList;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> recivedMessageList;


    public UserInfo(Long id, String email, String password, Set<Role> userRoles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;

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
}
