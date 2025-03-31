package com.ecommerce.chomoi.service;

import com.ecommerce.chomoi.dto.auth.*;
import com.ecommerce.chomoi.dto.jwt.JWTPayloadDto;
import com.ecommerce.chomoi.entities.Account;
import com.ecommerce.chomoi.enums.Role;
import com.ecommerce.chomoi.entities.RefreshToken;
import com.ecommerce.chomoi.exception.AppException;
import com.ecommerce.chomoi.mapper.AccountMapper;
import com.ecommerce.chomoi.repository.AccountRepository;
import com.ecommerce.chomoi.repository.RefreshTokenRepository;
import com.ecommerce.chomoi.util.PasswordUtil;
import com.ecommerce.chomoi.util.jwt.AccessTokenUtil;
import com.ecommerce.chomoi.util.jwt.RefreshTokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    PasswordUtil passwordUtil;
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    AccessTokenUtil accessTokenUtil;
    RefreshTokenUtil refreshTokenUtil;
    RefreshTokenRepository refreshTokenRepository;

    public void register(AuthRegisterRequest request) {
        boolean existedAccount = accountRepository.existsByEmailAndIsLocalTrue(request.getEmail());
        if(existedAccount){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");
        }
    }

    public AuthResponse verifyRegister(AuthRegisterRequest request) {
        // Find Account if not existed
        boolean existedAccount = accountRepository.existsByEmailAndIsLocalTrue(request.getEmail());
        if(existedAccount){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");
        }
        // Hash password
        String hashedPassword = passwordUtil.encodePassword(request.getPassword());
        request.setPassword(hashedPassword);

        // Roles for normal Account
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        Account account = Account.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .isLocal(true)
                .build();
        account.setRoles(roles);
        accountRepository.save(account);

        // Generate a pair of token
        String accessTokenString =  accessTokenUtil.generateToken(accountMapper.toJWTPayloadDto(account));
        String refreshTokenString =  refreshTokenUtil.generateToken(accountMapper.toJWTPayloadDto(account),account);
        return AuthResponse.builder()
                .accessToken(accessTokenString)
                .refreshToken(refreshTokenString)
                .build();
    }

    public AuthResponse login(AuthLoginRequest request){
        Account account = accountRepository.findByEmailAndIsLocalTrue(request.getEmail()).orElseThrow(
                ()-> new AppException(HttpStatus.NOT_FOUND, "Email Account not found", "auth-e-02")
        );
        if (!account.getRoles().contains(request.getRole())) {
            throw new AppException(HttpStatus.FORBIDDEN, "Insufficient permissions", "auth-e-08");
        }
        boolean isMatchPassword = passwordUtil.checkPassword(request.getPassword(), account.getPassword());
        if(!isMatchPassword){
            throw new AppException(HttpStatus.BAD_REQUEST, "Wrong password", "auth-e-03");
        }
        String accessTokenString =  accessTokenUtil.generateToken(accountMapper.toJWTPayloadDto(account));
        String refreshTokenString =  refreshTokenUtil.generateToken(accountMapper.toJWTPayloadDto(account),account);
        return AuthResponse.builder()
                .accessToken(accessTokenString)
                .refreshToken(refreshTokenString)
                .build();
    }

    public AuthResponse refreshToken(AuthRefreshTokenRequest request){
        String refreshTokenString = request.getRefreshToken();
        JWTPayloadDto payload = refreshTokenUtil.verifyToken(refreshTokenString);
        String accessTokenString =  accessTokenUtil.generateToken(payload);
        return AuthResponse.builder()
                .accessToken(accessTokenString)
                .build();
    }

    public void logOut(AuthLogOutRequest request){
        JWTPayloadDto payload = refreshTokenUtil.verifyToken(request.getRefreshToken());
        RefreshToken refreshToken =  refreshTokenRepository
                .findByAccountId(payload.getId())
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Refresh token not found", "auth-e-04")
                );
        refreshToken.setToken(null);
        refreshTokenRepository.save(refreshToken);
    }

    public void changePassword(String AccountId, AuthChangePasswordRequest request){
        Account Account = accountRepository
                .findById(AccountId)
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Account not found", "auth-e-05")
                );
        if(!Account.getIsLocal()){
            throw new AppException(HttpStatus.BAD_REQUEST, "Account is not internal", "auth-e-06");
        }
        boolean isMatchPassword = passwordUtil.checkPassword(request.getCurrentPassword(), Account.getPassword());
        if(!isMatchPassword){
            throw new AppException(HttpStatus.BAD_REQUEST, "Wrong current password", "auth-e-07");
        }
        String hashedNewPassword = passwordUtil.encodePassword(request.getNewPassword());
        Account.setPassword(hashedNewPassword);
        accountRepository.save(Account);
    }

    public AuthResponse loginOAuth2Success(OAuth2User oAuth2Account){
        String accountOAuthId = oAuth2Account.getAttribute("sub");
        String providerName = oAuth2Account.getAttribute("provider");
        Account account = accountRepository.findByIsLocalFalseAndProviderNameAndProviderId(providerName, accountOAuthId)
                .orElseGet(() -> {
                    Set<Role> roles = new HashSet<>();
                    roles.add(Role.USER);
                    Account newAccount = Account.builder()
                            .email(oAuth2Account.getAttribute("email"))
                            .displayName(oAuth2Account.getAttribute("name"))
                            .isLocal(false)
                            .providerId(accountOAuthId)
                            .providerName(providerName)
                            .avatar(oAuth2Account.getAttribute("picture"))
                            .roles(roles)
                            .build();
                    return accountRepository.save(newAccount);
                });

        JWTPayloadDto payload = accountMapper.toJWTPayloadDto(account);
        String accessToken = accessTokenUtil.generateToken(payload);
        String refreshToken = refreshTokenUtil.generateToken(payload, account);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void forgotPassword(AuthForgotPasswordRequest request){
        accountRepository.findByEmailAndIsLocalTrue(request.getEmail()).orElseThrow(
                ()-> new AppException(HttpStatus.NOT_FOUND, "Email account not found", "auth-e-02")
        );
    }

    public AuthResponse verifyForgotPassword(String email, AuthVerifyForgotPasswordRequest request){
        Account Account = accountRepository.findByEmailAndIsLocalTrue(email).orElseThrow(
                ()-> new AppException(HttpStatus.NOT_FOUND, "Email account not found", "auth-e-02")
        );
        String hashedPassword = passwordUtil.encodePassword(request.getNewPassword());
        Account.setPassword(hashedPassword);
        accountRepository.save(Account);
        String accessTokenString =  accessTokenUtil.generateToken(accountMapper.toJWTPayloadDto(Account));
        String refreshTokenString =  refreshTokenUtil.generateToken(accountMapper.toJWTPayloadDto(Account),Account);
        return AuthResponse.builder()
                .accessToken(accessTokenString)
                .refreshToken(refreshTokenString)
                .build();
    }

    public AuthAccountInfoResponse getAccountInfo(String accountId) {
        Account Account = accountRepository
                .findById(accountId)
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Account not found", "auth-e-05")
                );
        return accountMapper.toAccountInfo(Account);
    }
}
