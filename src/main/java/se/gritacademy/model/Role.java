package se.gritacademy.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    Role() {
    }

    ;

    @Override
    public String getAuthority() {
        return name();
    }
}
