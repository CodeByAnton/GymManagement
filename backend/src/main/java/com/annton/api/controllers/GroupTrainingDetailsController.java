package com.annton.api.controllers;

import com.annton.api.dto.GroupTrainingProgramDetailsDTO;
import com.annton.api.dto.IdDTO;
import com.annton.api.services.GroupTrainingService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group-training-details")
@RequiredArgsConstructor
public class GroupTrainingDetailsController {
    private static final Logger logger = LogManager.getLogger(GroupTrainingDetailsController.class);
    private final GroupTrainingService groupTrainingService;


    @GetMapping
    public ResponseEntity<List<GroupTrainingProgramDetailsDTO>> getAllScheduledTrainings() {
        logger.info("GET request to /api/v1/group-training-details endpoint accepted");
        return ResponseEntity.ok(groupTrainingService.getAllProgramDetails());
    }


    @PostMapping
    public ResponseEntity<IdDTO> createProgramDetails(@RequestBody GroupTrainingProgramDetailsDTO dto) {
        logger.info("POST request to /api/v1/group-training-details endpoint accepted");
        return ResponseEntity.ok(groupTrainingService.createGroupTrainingDetails(dto));
    }
}
