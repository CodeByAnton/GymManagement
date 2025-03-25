package com.annton.api.services;

import com.annton.api.data.entities.PersonalService;
import com.annton.api.data.enums.EventStatus;
import com.annton.api.data.enums.Role;
import com.annton.api.data.enums.ServiceType;
import com.annton.api.data.repositories.PersonalServiceRepository;
import com.annton.api.dto.*;
import com.annton.api.services.base.RandomBasedTokenGeneration;
import com.annton.api.services.exceptions.NotFound;
import com.annton.api.services.exceptions.ProhibitedOperationException;
import com.annton.api.services.exceptions.RoleMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PersonalServiceService extends RandomBasedTokenGeneration {
    private final PersonalServiceRepository personalServiceRepository;
    private final NotificationService notificationService;

    private GroupTrainingService groupTrainingService;

    @Autowired
    public void setGroupTrainingService(@Lazy GroupTrainingService groupTrainingService) {
        this.groupTrainingService = groupTrainingService;
    }

    private final UserService userService;

    public OperationInfo createPersonalService(PersonalServiceDTO dto) throws NotFound {
        var trainer = userService.getUserById(userService.getCurrentUser().getId());
        if (!trainer.getRole().equals(Role.TRAINER)) {
            throw new RoleMismatchException("Assigning a trainer with an invalid role");
        }
        var token = generateToken(6);
        var client = userService.getUserByEmail(dto.getClientEmail());
        var providedDateTime = LocalDateTime.parse(dto.getStartDateTime());
        if(providedDateTime.isBefore(LocalDateTime.now())){
            throw new ProhibitedOperationException("Prohibited operation");
        }
        if(hasConflictInTrainerSchedule(trainer.getId(), providedDateTime, dto.getDuration())){
            throw new ProhibitedOperationException("Trainer has another personal service assigned to him at this time");
        }
        if(hasConflictInClientSchedule(client.getId(), providedDateTime, dto.getDuration())){
            throw new ProhibitedOperationException("Client has another personal service assigned to him at this time");
        }
        if(groupTrainingService.hasConflictInClientSchedule(client.getId(), providedDateTime, dto.getDuration())){
            throw new ProhibitedOperationException("Client has a group training at this time");
        }
        if(groupTrainingService.hasConflictInTrainerSchedule(trainer.getId(), providedDateTime, dto.getDuration())){
            throw new ProhibitedOperationException("Trainer has a group training assigned to him at this time");
        }

        var personalService = PersonalService.builder().status(EventStatus.SCHEDULED).
                serviceType(ServiceType.valueOf(dto.getServiceType())).
                startDateTime(providedDateTime).
                endDateTime(providedDateTime.plusMinutes(dto.getDuration())).
                trainer(trainer).client(client).passCode(token).build();
        personalServiceRepository.save(personalService);
        notificationService.createNotification(
                NotificationDTO.builder().senderEmail("SYSTEM").receiverEmail(client.getEmail()).
                        message("You have booked training with: "
                                + trainer.getEmail()
                                + " it's pass code is: "+ token)
                        .build()
        );
        return new OperationInfo(true, "personal service created");
    }

    public PaginatedPersonalServiceDTO getAllPersonalServicesOfCurrentUser(PageRequest pageable) {
        var currentUser = userService.getCurrentUser();
        var currentUserRole = currentUser.getRole();
        if (currentUserRole != Role.TRAINER && currentUserRole != Role.CLIENT) {
            throw new ProhibitedOperationException("Acquiring a personal service of an invalid role");
        }
        Page<PersonalService> personalServicePage = null;
        if(currentUserRole == Role.TRAINER){
            personalServicePage = personalServiceRepository.
                    findByTrainer_IdAndStatus(currentUser.getId(), pageable, EventStatus.SCHEDULED);
        }else{
            personalServicePage = personalServiceRepository.
                    findByClient_IdAndStatus(currentUser.getId(), pageable, EventStatus.SCHEDULED);
        }
        return PaginatedPersonalServiceDTO.builder().
                personalServices(personalServicePage.getContent().
                        stream().map(PersonalService::includePassCode).toList()).
                totalPages(personalServicePage.getTotalPages()).build();
    }

    public OperationInfo changePersonalServiceStatus(ChangePersonalServiceStatusDTO dto) {
        var user = userService.getCurrentUser();
        var personalServiceOptional = personalServiceRepository.findPersonalServiceById(dto.getPersonalServiceId());
        if(personalServiceOptional.isEmpty()){
            throw new NotFound("Personal service not found");
        }
        var personalService = personalServiceOptional.get();
        if((!(personalService.getTrainer().getId() == user.getId())) &&
                (!(personalService.getClient().getId() == user.getId()))){
            throw new ProhibitedOperationException("Users can only change status of their own personal services");
        }
        personalService.setStatus(EventStatus.valueOf(dto.getNewEventStatus()));
        personalServiceRepository.save(personalService);
        return new OperationInfo(true, "Event status changed");
    }

    public boolean hasConflictInClientSchedule(int clientId, LocalDateTime startDateTime, LocalDateTime endDateTime){
        return !personalServiceRepository.findClientScheduleConflict(clientId
                , startDateTime, endDateTime).isEmpty();
    }

    public boolean hasConflictInClientSchedule(int clientId, LocalDateTime startDateTime, int duration){
        return hasConflictInClientSchedule(clientId
                , startDateTime, startDateTime.plusMinutes(duration));
    }

    public boolean hasConflictInTrainerSchedule(int trainer, LocalDateTime startDateTime, int duration){
        return !personalServiceRepository.findTrainerScheduleConflict(trainer
                , startDateTime, startDateTime.plusMinutes(duration)).isEmpty();
    }
}
