package com.annton.api.controllers;

import com.annton.api.dto.*;
import com.annton.api.services.JwtService;
import com.annton.api.services.OtpService;
import com.annton.api.services.RefreshTokenService;
import com.annton.api.services.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final OtpService otpService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<OperationInfo> register(@RequestBody final RegisterRequest registerRequest)
            throws BadCredentialsException, MessagingException {
        logger.info("POST request to /api/v1/auth/register endpoint accepted");
        var user = userService.registerUser(registerRequest);

        otpService.sendOtpToken(user.getEmail());

        return ResponseEntity.ok(
                new OperationInfo(
                        true,
                        "One time password send to your email")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<OperationInfo> login(@RequestBody final LoginRequest loginRequest)
            throws BadCredentialsException, MessagingException {
        logger.info("POST request to /api/v1/auth/register/login endpoint accepted");
        var user = userService.checkPassword(loginRequest);

        otpService.sendOtpToken(user.getEmail());

        return ResponseEntity.ok(
                new OperationInfo(
                        true,
                        "One time password send to your email")
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenDTO> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO)
                    throws BadCredentialsException {
        logger.info("POST request to /api/v1/auth/register/refresh endpoint accepted");
        return ResponseEntity.ok(refreshTokenService.refresh(refreshTokenDTO));
    }

    @PostMapping("/send-otp/{email}")
    public ResponseEntity<OperationInfo> sendMail(@PathVariable final String email)
            throws BadCredentialsException, MessagingException {
        logger.info("POST request to /api/v1/auth/send-otp/{email} endpoint accepted");
        otpService.sendOtpToken(email);
        return ResponseEntity.ok(new OperationInfo(true, "OTP sent"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<OperationInfo> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO)
            throws BadCredentialsException {
        logger.info("POST request to /api/v1/auth/reset-password endpoint accepted");
        return ResponseEntity.ok(refreshTokenService.resetPassword(resetPasswordDTO));
    }



    @PostMapping("/logout")
    public ResponseEntity<OperationInfo> logout(@RequestBody RefreshTokenDTO refreshTokenDTO)
            throws BadCredentialsException {
        logger.info("POST request to /api/v1/auth/logout endpoint accepted");
        return ResponseEntity.ok(refreshTokenService.logout(refreshTokenDTO));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthenticationResponse> verifyOtp(@RequestBody final VerifyOtpDTO verifyOtpDTO)
            throws BadCredentialsException {
        logger.info("POST request to /api/v1/auth/verify-otp endpoint accepted");
        var user = otpService.verifyOtp(verifyOtpDTO);

        var response =
                jwtService.createAuthenticationResponse(user.getEmail(), user.getRole());

        return ResponseEntity.ok(response);
    }

}
