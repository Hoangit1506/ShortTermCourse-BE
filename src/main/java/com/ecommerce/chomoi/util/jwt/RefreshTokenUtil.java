package com.ecommerce.chomoi.util.jwt;

import com.ecommerce.chomoi.dto.jwt.JWTPayloadDto;
import com.ecommerce.chomoi.entities.Account;
import com.ecommerce.chomoi.exception.AppException;
import com.ecommerce.chomoi.entities.RefreshToken;
import com.ecommerce.chomoi.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RefreshTokenUtil extends BaseJWTUtil {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${app.jwt.refresh.expiration}")
    private long refreshExpiration;

    @Override
    protected String getSecret() {
        return refreshSecret;
    }

    @Override
    protected long getExpiration() {
        return refreshExpiration;
    }

    public String generateToken(JWTPayloadDto payload, Account account) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByAccount(account)
                .orElseGet(() -> RefreshToken.builder().account(account).build());
        String token = super.generateToken(payload);
        refreshToken.setToken(token);
        refreshTokenRepository.save(refreshToken);
        return token;
    }


    @Override
    public JWTPayloadDto verifyToken(String token) {
        JWTPayloadDto payload = super.verifyToken(token);
        RefreshToken refreshToken =  refreshTokenRepository
                .findByAccountId(payload.getId())
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Refresh token not found", "auth-e-01")
                );
        if(refreshToken.getToken() == null || !refreshToken.getToken().equals(token)){
            throw new AppException(HttpStatus.NOT_FOUND, "Refresh token not found", "auth-e-01");
        }
        return payload;
    }
}
