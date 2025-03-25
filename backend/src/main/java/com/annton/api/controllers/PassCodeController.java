package com.annton.api.controllers;

import com.annton.api.dto.PassCodeDTO;
import com.annton.api.services.PassCodeTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pass-code")
@RequiredArgsConstructor
public class PassCodeController {
    private static final Logger logger = LogManager.getLogger(PassCodeController.class);
    private final PassCodeTokenService passCodeTokenService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<PassCodeDTO> sendPassCodeToken(@RequestParam String to)
            throws MessagingException {
        logger.info("POST request to /api/v1/pass-code endpoint accepted");
        return ResponseEntity.ok(passCodeTokenService.createAndSendPassCodeToken(to));
    }

}
