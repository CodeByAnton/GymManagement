package com.annton.api.controllers;


import com.annton.api.dto.DTOWrapper;
import com.annton.api.dto.OperationInfo;
import com.annton.api.dto.SubscriptionDTO;
import com.annton.api.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionServiceController {
    private static final Logger logger = LogManager.getLogger(SubscriptionServiceController.class);
    private final SubscriptionService subscriptionService;
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<OperationInfo> createSubscription(
            @RequestBody SubscriptionDTO subscriptionDTO
            ){
        logger.info("POST request to /api/v1/subscription endpoint accepted");
        return ResponseEntity.status(HttpStatus.CREATED).
                body(subscriptionService.
                        createSubscription(subscriptionDTO));
    }


    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<DTOWrapper<SubscriptionDTO>> getSubscriptionOfCurrentUser(){
        return ResponseEntity.status(HttpStatus.OK).body(
                subscriptionService.getSubscriptionOfCurrentUser()
        );
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DTOWrapper<SubscriptionDTO>> getSubscriptionOfUserByEmail(@PathVariable String email){
        return ResponseEntity.status(HttpStatus.OK).body(
                subscriptionService.getSubscriptionOfUserByEmail(email)
        );
    }

}
