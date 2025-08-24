package com.rodiugurlu.authservice.service.impl;

import com.rodiugurlu.authservice.dto.DtoUser;
import com.rodiugurlu.authservice.dto.RegisterRequest;
import com.rodiugurlu.authservice.dto.VerificationUserRequest;
import com.rodiugurlu.authservice.entity.AuthUser;
import com.rodiugurlu.authservice.entity.RefreshToken;
import com.rodiugurlu.authservice.entity.VerificationToken;
import com.rodiugurlu.authservice.enums.Role;
import com.rodiugurlu.authservice.kafka.event_model.UserCreatedEvent;
import com.rodiugurlu.authservice.jwt.AuthRequest;
import com.rodiugurlu.authservice.jwt.AuthResponse;
import com.rodiugurlu.authservice.jwt.JwtService;
import com.rodiugurlu.authservice.kafka.KafkaProducerService;
import com.rodiugurlu.authservice.kafka.event_model.UserDeletedEvent;
import com.rodiugurlu.authservice.repository.AuthUserRepository;
import com.rodiugurlu.authservice.repository.VerificationTokenRepository;
import com.rodiugurlu.authservice.service.IAuthUserService;
import com.rodiugurlu.authservice.service.IEmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements IAuthUserService {
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final IEmailService emailService;
    private final KafkaProducerService kafkaProducerService;


    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authenticationProvider.authenticate(authenticationToken);
            Optional<AuthUser> optional = authUserRepository.findByUsername(request.getUsername());
            if (optional.isPresent()) {
                String accessToken = jwtService.generateToken(optional.get());
                RefreshToken refreshToken = createRefreshToken(optional.get());
                return new AuthResponse(accessToken, refreshToken.getRefreshToken());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void initiateRegister(RegisterRequest request) {
        if (authUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("this username already taken");
        }
        if (authUserRepository.findByEmail(request.getUsername()).isPresent()) {
            throw new RuntimeException("this username already taken");
        }
        String code = generateVerificationCode();
        verificationTokenRepository.save(
                VerificationToken.builder()
                        .email(request.getEmail())
                        .username(request.getUsername())
                        .fullName(request.getFullName())
                        .birthDate(request.getBirthDate())
                        .phone(request.getPhone())
                        .enabled(true)
                        .locked(false)
                        .encodedPassword(passwordEncoder.encode(request.getPassword()))
                        .code(code)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(3))
                        .build()
        );
        emailService.sendVerificationEmail(request.getEmail(), code);
    }

    @Override
    @Transactional
    public DtoUser completeUserRegisteration(VerificationUserRequest request) {
        VerificationToken token = verificationTokenRepository.findByEmailAndCode(request.getEmail(), request.getVerificationCode());
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("tokenin suresi gecmistir");
        }
        Set<Role> roleList = new HashSet<>();
        roleList.add(Role.USER);
        AuthUser authUser = new AuthUser();
        authUser.setEmail(token.getEmail());
        authUser.setUsername(token.getUsername());
        authUser.setPassword(token.getEncodedPassword());
        authUser.setRoles(roleList);
        authUserRepository.save(authUser);
        DtoUser dtoUser = new DtoUser();
        BeanUtils.copyProperties(authUser, dtoUser);
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(
                token.getUsername(),
                token.getEmail(),
                token.getFullName(),
                token.getPhone(),
                true,
                token.getBirthDate(),
                LocalDateTime.now()
        );
        kafkaProducerService.sendUserCreated(userCreatedEvent);
        verificationTokenRepository.deleteAllByEmail(token.getEmail());
        return dtoUser;
    }

    private RefreshToken createRefreshToken(AuthUser user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4));
        return refreshToken;
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        try {
            UserDeletedEvent userDeletedEvent = new UserDeletedEvent(username);
            kafkaProducerService.sendUserDeleted(userDeletedEvent);

            authUserRepository.deleteByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
