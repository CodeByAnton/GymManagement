package com.annton.api.services;


import com.annton.api.data.entities.RefreshToken;
import com.annton.api.data.repositories.RefreshTokenRepository;
import com.annton.api.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;


    public AccessTokenDTO refresh(RefreshTokenDTO refreshTokenDTO) throws BadCredentialsException {
        var token = refreshTokenDTO.getRefreshToken();
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            throw new BadCredentialsException("Compromised refresh token");
        });
        var email = jwtService.getEmailFromToken(token);
        var role = jwtService.getRoleFromToken(token);
        return jwtService.createAccessTokenDTO(email, role);
    }

    public OperationInfo logout(RefreshTokenDTO refreshTokenDTO) {
        var token = refreshTokenDTO.getRefreshToken();
        var email = jwtService.getEmailFromToken(token);
        var user = userService.getUserByEmail(email);
        refreshTokenRepository.save(RefreshToken.builder().
                token(token).
                user(user).build());
        return new OperationInfo(true, "You have logged out successfully");
    }

    @Transactional
    public OperationInfo resetPassword(ResetPasswordDTO resetPasswordDTO) {

        var user = otpService.verifyOtp(
                new VerifyOtpDTO(resetPasswordDTO.getOtp(),
                resetPasswordDTO.getEmail())
        );

        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));

        return new OperationInfo(true, "Your password has been reset");

    }
}
