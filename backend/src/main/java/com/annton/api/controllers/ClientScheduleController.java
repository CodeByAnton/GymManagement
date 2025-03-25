package com.annton.api.controllers;

import com.annton.api.dto.PaginatedGroupTrainingDTO;
import com.annton.api.dto.PaginatedPersonalServiceDTO;
import com.annton.api.services.GroupTrainingService;
import com.annton.api.services.PersonalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client/schedule")
@RequiredArgsConstructor
public class ClientScheduleController {

    private final PersonalServiceService personalServiceService;
    private final GroupTrainingService groupTrainingService;

    @GetMapping("/group-trainings")
    public PaginatedGroupTrainingDTO getClientGroupTrainings(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size){
        var pageable = PageRequest.of(page, size);
        return groupTrainingService.getAllBookedGroupTrainingsOfCurrentClient(pageable);
    }
    @GetMapping("/personal-services")
    public PaginatedPersonalServiceDTO getClientPersonalServices(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        var pageable = PageRequest.of(page, size);
        return personalServiceService.getAllPersonalServicesOfCurrentUser(pageable);
    }
}
