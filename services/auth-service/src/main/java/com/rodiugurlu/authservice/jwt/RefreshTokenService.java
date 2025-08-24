package com.rodiugurlu.authservice.jwt;

import com.rodiugurlu.authservice.entity.AuthUser;
import com.rodiugurlu.authservice.entity.RefreshToken;
import com.rodiugurlu.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;

    private JwtService jwtService;

    public boolean isRefreshTokenExpired(Date expiredDate) {
        return new Date().before(expiredDate);
    }

    private RefreshToken createRefreshToken(AuthUser user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4));
        refreshToken.setUser(user);

        return refreshToken;
    }

    //sjkfaskf ksjf askjf aksjf kjsldfkjl
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> optional = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
        if (optional.isEmpty()) {
            System.out.println("REFRESH TOKEN GEÇERSİZDİR : " + request.getRefreshToken());
        }

        RefreshToken refreshToken = optional.get();

        if (!isRefreshTokenExpired(refreshToken.getExpireDate())) {
            System.out.println("REFRESH TOKEN EXPİRE OLMUŞTUR BABA : " + request.getRefreshToken());
        }

        String accessToken = jwtService.generateToken(refreshToken.getUser());
        RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(refreshToken.getUser()));

        // accesss 2
        // refresh 1

        return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
    }
}
