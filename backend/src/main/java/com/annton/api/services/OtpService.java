package com.annton.api.services;

import com.annton.api.data.entities.OtpToken;
import com.annton.api.data.entities.User;
import com.annton.api.data.repositories.OtpRepository;
import com.annton.api.dto.VerifyOtpDTO;
import com.annton.api.services.base.RandomBasedTokenGeneration;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService extends RandomBasedTokenGeneration {
    private final OtpRepository otpRepository;
    private final MailService mailService;
    private final UserService userService;
    private final static int otpTokenSize = 6;


    public Optional<OtpToken> getOtpTokenByEmail(String email) {
        return otpRepository.findByUser_email(email);
    }

    public OtpToken createOtpToken(String email) throws BadCredentialsException {
        otpRepository.deleteByUser_email(email);
        var otp = OtpToken.builder().token(generateToken(otpTokenSize)).
                user(userService.getUserByEmail(email)).build();
        return otpRepository.save(otp);
    }

    public void sendOtpToken(String email) throws MessagingException {
        var otp = createOtpToken(email);
        mailService.sendConfirmationEmail(email, otp.getToken());
    }

    @Transactional
    public User verifyOtp(VerifyOtpDTO verifyOtpDTO) throws BadCredentialsException{
        var optionalOtp = getOtpTokenByEmail(verifyOtpDTO.getEmail());

        if(optionalOtp.isEmpty())
            throw new BadCredentialsException("Invalid email");

        var otp = optionalOtp.get();

        if(!otp.getToken().equals(verifyOtpDTO.getOtp()))
            throw new BadCredentialsException("Invalid OTP");

        if((new Timestamp(System.currentTimeMillis())).after(otp.getExpiresAt()))
            throw new BadCredentialsException("OTP has expired");

        var user = otp.getUser();
        user.setConfirmed(true);
        return user;
    }

}
