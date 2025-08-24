package com.rodiugurlu.authservice.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUser {
    private int id;
    private String username;
    private String email;
    private Boolean locked;
    private Boolean enabled;
}
