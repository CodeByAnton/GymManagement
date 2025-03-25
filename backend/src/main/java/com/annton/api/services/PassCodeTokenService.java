package com.annton.api.services;

import com.annton.api.data.entities.PassCodeToken;
import com.annton.api.data.enums.Role;
import com.annton.api.data.repositories.PassCodeTokenRepository;
import com.annton.api.dto.PassCodeDTO;
import com.annton.api.services.base.RandomBasedTokenGeneration;
import com.annton.api.services.exceptions.RoleMismatchException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassCodeTokenService extends RandomBasedTokenGeneration {
    private final MailService mailService;
    private final UserService userService;
    private final PassCodeTokenRepository passCodeTokenRepository;
    private final static int passCodeTokenSize = 6;

    public PassCodeDTO createAndSendPassCodeToken(String userEmail) throws MessagingException {
        var admin = userService.getCurrentUser();
        if(admin == null || !admin.getRole().equals(Role.ADMINISTRATOR)){
            throw new RoleMismatchException("Only admins can send pass codes");
        }
        var client = userService.getUserByEmail(userEmail);
        passCodeTokenRepository.deletePassCodeTokenByUser_id(client.getId());
        var passCodeToken = PassCodeToken.builder().
                token(generateToken(passCodeTokenSize)).
                user(client).build();
        passCodeTokenRepository.save(passCodeToken);
        mailService.sendPassCodeEmail(userEmail, passCodeToken.getToken());
        return new PassCodeDTO(passCodeToken.getToken());
    }

}
