package com.annton.api.controllers;

import com.annton.api.data.enums.EventStatus;
import com.annton.api.dto.*;
import com.annton.api.services.GroupTrainingService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/group-training")
@RequiredArgsConstructor
public class GroupTrainingController {
    private static final Logger logger = LogManager.getLogger(GroupTrainingController.class);
    private final GroupTrainingService groupTrainingService;

    @GetMapping
    @PreAuthorize("hasRole('TRAINER') || hasRole('USER')")
    public ResponseEntity<PaginatedGroupTrainingDTO> getAllScheduledTrainings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "SCHEDULED") String eventStatus) {
        logger.info("GET request to /api/v1/group-training endpoint accepted");
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(groupTrainingService.getAllScheduledTrainings(pageable, EventStatus.valueOf(eventStatus)));
    }

    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<OperationInfo> createGroupTraining(@RequestBody GroupTrainingDTO dto) {
        logger.info("POST request to /api/v1/group-training endpoint accepted");
        return ResponseEntity.status(HttpStatus.OK).
                body(groupTrainingService.createGroupTraining(dto));
    }

    @PostMapping("/status")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<OperationInfo> changeGroupTrainingStatus(@RequestBody ChangeStatusGroupTrainingDTO dto) {
        logger.info("POST request to /api/v1/group-training/status endpoint accepted");
        return ResponseEntity.status(HttpStatus.OK).
                body(groupTrainingService.changeStatusGroupTraining(dto));
    }


    @PostMapping("/book")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OperationInfo> bookGroupTraining(@RequestBody BookGroupTrainingDTO dto) {
        logger.info("POST request to /api/v1/group-training/book endpoint accepted");
        return ResponseEntity.status(HttpStatus.OK).
                body(groupTrainingService.bookGroupTraining(dto));
    }

    @GetMapping("/book")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PaginatedBookingDTO> getAllBookingsOfClient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET request to /api/v1/group-training/book endpoint accepted");
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).
                body(groupTrainingService.getAllBookingsOfCurrentClient(pageable));
    }

    @PostMapping("/book/status")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<OperationInfo> cancelBookGroupTraining(@RequestBody ChangeBookingStatusDTO dto) {
        logger.info("POST request to /api/v1/group-training/book/status endpoint accepted");
        return ResponseEntity.status(HttpStatus.OK).
                body(groupTrainingService.changeBookingStatus(dto));
    }

}
