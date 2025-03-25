package com.annton.api.controllers;

import com.annton.api.dto.IdDTO;
import com.annton.api.dto.NotificationDTO;
import com.annton.api.dto.OperationInfo;
import com.annton.api.dto.PaginatedNotificationDTO;
import com.annton.api.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private static final Logger logger = LogManager.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<PaginatedNotificationDTO> getNotificationsOfCurrentUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        logger.info("GET request to /api/v1/notifications endpoint accepted");
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(notificationService.getNotificationsOfCurrentUser(pageable));
    }

    @PostMapping("/check")
    public ResponseEntity<OperationInfo> checkNotification(
            @RequestBody IdDTO idDTO
            ){
        logger.info("POST request to /api/v1/notifications/check endpoint accepted");
        return ResponseEntity.status(HttpStatus.OK).
                body(notificationService.checkNotification(idDTO));
    }

    @PostMapping
    public ResponseEntity<OperationInfo> createNotification(
            @RequestBody NotificationDTO notificationDTO
    ){
        logger.info("POST request to /api/v1/notifications endpoint accepted");
        return ResponseEntity.status(HttpStatus.CREATED).
                body(notificationService.createNotification(notificationDTO));
    }



}
