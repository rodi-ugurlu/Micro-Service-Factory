package com.rodiugurlu.authservice.controller;

import com.rodiugurlu.authservice.dto.RegisterRequest;
import com.rodiugurlu.authservice.dto.VerificationUserRequest;
import com.rodiugurlu.authservice.jwt.AuthRequest;
import com.rodiugurlu.authservice.jwt.AuthResponse;
import com.rodiugurlu.authservice.service.IAuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthUserController {
    private final IAuthUserService authUserService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authUserService.authenticate(request));
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authUserService.initiateRegister(registerRequest);
        return ResponseEntity.ok("Doğrulama kodu gönderildi!");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@Valid @RequestBody VerificationUserRequest verificationRequest) {
        authUserService.completeUserRegisteration(verificationRequest);
        return ResponseEntity.ok("Kayıt tamamlandı!");
    }

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        authUserService.deleteUser(username);
        return ResponseEntity.ok("Kullanıcı silindi baba bura auth service");
    }
}
