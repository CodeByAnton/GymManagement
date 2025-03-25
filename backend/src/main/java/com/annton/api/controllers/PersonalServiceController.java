package com.annton.api.controllers;

import com.annton.api.dto.ChangePersonalServiceStatusDTO;
import com.annton.api.dto.OperationInfo;
import com.annton.api.dto.PaginatedPersonalServiceDTO;
import com.annton.api.dto.PersonalServiceDTO;
import com.annton.api.services.PersonalServiceService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/personal-services")
@RequiredArgsConstructor
public class PersonalServiceController {
    private static final Logger logger = LogManager.getLogger(PersonalServiceController.class);
    private final PersonalServiceService personalServiceService;

    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<OperationInfo> createPersonalService(
            @RequestBody PersonalServiceDTO personalServiceDTO) {
        logger.info("POST request to /api/v1/personal-services endpoint accepted");
        return ResponseEntity.status(HttpStatus.CREATED).
                body(personalServiceService.
                        createPersonalService(personalServiceDTO));
    }

    @PostMapping("/status")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<OperationInfo> changePersonalServiceStatus(
            @RequestBody ChangePersonalServiceStatusDTO dto) {
        logger.info("POST request to /api/v1/personal-services/status endpoint accepted");
        return ResponseEntity.status(HttpStatus.CREATED).
                body(personalServiceService.
                        changePersonalServiceStatus(dto));
    }


    @GetMapping
    @PreAuthorize("hasRole('TRAINER') || hasRole('CLIENT')")
    public ResponseEntity<PaginatedPersonalServiceDTO> getAllPersonalServicesOfCurrentUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET request to /api/v1/personal-services endpoint accepted");
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(
                personalServiceService.getAllPersonalServicesOfCurrentUser(pageable));
    }
}
