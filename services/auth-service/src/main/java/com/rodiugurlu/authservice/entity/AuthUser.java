package com.rodiugurlu.authservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodiugurlu.authservice.enums.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuthUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) role::name)
                .toList();
    }

}
