package com.annton.api.controllers;

import com.annton.api.dto.ApplicationDTO;
import com.annton.api.dto.ConsiderApplicationDTO;
import com.annton.api.dto.OperationInfo;
import com.annton.api.dto.PostApplicationDTO;
import com.annton.api.services.ApplicationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private static final Logger logger = LogManager.getLogger(ApplicationController.class);
    private final ApplicationService applicationService;


    @PostMapping
    public ResponseEntity<OperationInfo>
        createApplication(@RequestBody final PostApplicationDTO postApplicationDTO) throws MessagingException {
        logger.info("POST request to /api/v1/applications endpoint accepted");
        return ResponseEntity.ok(applicationService.postNewApplication(postApplicationDTO));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getApplications() {
        logger.info("GET request to /api/v1/applications endpoint accepted");
        return ResponseEntity.ok(applicationService.getApplications());
    }

    @PostMapping("/consider")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<OperationInfo>
    considerApplication(@RequestBody final ConsiderApplicationDTO considerApplicationDTO) {
        logger.info("POST request to /api/v1/applications/consider endpoint accepted");
        return ResponseEntity.ok(applicationService.considerApplication(considerApplicationDTO));
    }

}
