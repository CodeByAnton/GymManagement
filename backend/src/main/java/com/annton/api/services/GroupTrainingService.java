package com.annton.api.services;

import com.annton.api.data.entities.GroupTraining;
import com.annton.api.data.entities.GroupTrainingBooking;
import com.annton.api.data.entities.GroupTrainingProgramDetails;
import com.annton.api.data.enums.BookingStatus;
import com.annton.api.data.enums.EventStatus;
import com.annton.api.data.enums.Role;
import com.annton.api.data.repositories.GroupTrainingBookingRepository;
import com.annton.api.data.repositories.GroupTrainingProgramDetailsRepository;
import com.annton.api.data.repositories.GroupTrainingRepository;
import com.annton.api.dto.*;
import com.annton.api.services.base.RandomBasedTokenGeneration;
import com.annton.api.services.exceptions.NotFound;
import com.annton.api.services.exceptions.ProhibitedOperationException;
import com.annton.api.services.exceptions.RoleMismatchException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupTrainingService extends RandomBasedTokenGeneration {
    private final UserService userService;
    private final NotificationService notificationService;
    private final GroupTrainingRepository groupTrainingRepository;
    private final GroupTrainingProgramDetailsRepository groupTrainingProgramDetailsRepository;
    private final GroupTrainingBookingRepository bookingRepository;
    private final PersonalServiceService personalServiceService;
    private final GroupTrainingBookingRepository groupTrainingBookingRepository;

    public boolean hasConflictInClientSchedule(int clientId, LocalDateTime startTime, LocalDateTime endTime){
        return !bookingRepository.
                findClientGroupTrainingScheduleConflict(clientId, startTime, endTime).
                isEmpty();
    }

    public boolean hasConflictInClientSchedule(int clientId, LocalDateTime startTime, int duration){
        return hasConflictInClientSchedule(clientId, startTime, startTime.plusMinutes(duration));
    }

    public boolean hasConflictInTrainerSchedule(int trainerId, LocalDateTime startTime, int duration){
        return !groupTrainingRepository.
                findTrainerGroupTrainingScheduleConflict(trainerId, startTime, startTime.plusMinutes(duration)).
                isEmpty();
    }

    @Transactional
    public OperationInfo createGroupTraining(GroupTrainingDTO dto) {
        var trainer = userService.getCurrentUser();

        if (trainer == null || !trainer.getRole().equals(Role.TRAINER)) {
            throw new RoleMismatchException("Attempt to assign non-existing user or user with inappropriate role on trainer position");
        }

        GroupTrainingProgramDetails programDetails;

        var optionalProgramDetails = groupTrainingProgramDetailsRepository.
                findById(Integer.parseInt(dto.getProgramDetailsId()));
        if (optionalProgramDetails.isEmpty()) {
            throw new NotFound("There is no group training program details with id " + dto.getProgramDetailsId());
        }
        programDetails = optionalProgramDetails.get();
        var startTime = LocalDateTime.parse(dto.getStartDateTime());

        if(personalServiceService.hasConflictInTrainerSchedule(trainer.getId(), startTime, dto.getDuration())){
            throw new ProhibitedOperationException("Trainer has personal service assigned to him at this time");
        }

        if(hasConflictInTrainerSchedule(trainer.getId(), startTime, dto.getDuration())){
            throw new ProhibitedOperationException("Trainer has group training assigned to him at this time");
        }

        var groupTraining = GroupTraining.builder().
                startDateTime(startTime).
                endDateTime(startTime.plusMinutes(dto.getDuration())).
                status(EventStatus.SCHEDULED).
                maxParticipants(dto.getMaxParticipants()).passCode(generateToken(6)).
                trainer(trainer)
                .details(programDetails).build();

        groupTrainingRepository.save(groupTraining);

        return new OperationInfo(true, "Group training succesfully created");
    }

    public OperationInfo changeBookingStatus(ChangeStatusGroupTrainingDTO dto){
        var user = userService.getCurrentUser();
        if(!bookingRepository.isClientBookedOnGroupTraining(user.getId(), dto.getGroupTrainingId())){
            return new OperationInfo(false, "Пользователь не записан на эту тренировку");
        }
        bookingRepository.
                cancelBookingByClientAndGroupTraining(user.getId(), dto.getGroupTrainingId());
        return new OperationInfo(true, "Запись успешно отменена");
    }

    public OperationInfo changeStatusGroupTraining(ChangeStatusGroupTrainingDTO dto) {
        var user = userService.getCurrentUser();
        if(user.getRole().equals(Role.CLIENT)){
            return changeBookingStatus(dto);
        }
        if (!user.getRole().equals(Role.TRAINER)) {
            throw new RoleMismatchException("Only trainers are able to change status of group trainings");
        }
        var groupTrainingOptional = groupTrainingRepository.findGroupTrainingById(dto.getGroupTrainingId());
        if (groupTrainingOptional.isEmpty()) {
            throw new NotFound("Group training not found");
        }
        var groupTraining = groupTrainingOptional.get();
        if (!(groupTraining.getTrainer().getId() == user.getId())) {
            throw new ProhibitedOperationException("Trainers can only change status of their own  trainings");
        }
        groupTraining.setStatus(EventStatus.valueOf(dto.getNewEventStatus()));
        groupTrainingRepository.save(groupTraining);
        return new OperationInfo(true, "Event status changed");
    }

    public PaginatedGroupTrainingDTO getAllScheduledTrainings(Pageable pageable, EventStatus eventStatus) {
        var user = userService.getCurrentUser();
        if (user == null || user.getRole().equals(Role.ADMINISTRATOR)) {
            throw new ProhibitedOperationException("Admins can not use this function");
        }
        Page<GroupTraining> page;

        if(user.getRole().equals(Role.TRAINER)){
            page = groupTrainingRepository.findByTrainer_idAndStatus(user.getId(), eventStatus, pageable);
            return PaginatedGroupTrainingDTO.builder().totalPages(page.getTotalPages()).
                    groupTrainings(page.get().map(GroupTraining::includePassCode).toList()).build();
        }else{
            page = groupTrainingRepository.findAllByStartTimeAfterAndNotCancelled(LocalDateTime.now(), pageable);
            return PaginatedGroupTrainingDTO.builder().totalPages(page.getTotalPages()).
                    groupTrainings(page.get().map(
                            (groupTraining) -> {
                                var clientBookedThisTraining = groupTrainingBookingRepository.
                                        isClientBookedOnGroupTraining(user.getId(), groupTraining.getId());
                                return groupTraining.includeClientHasBooking(clientBookedThisTraining);
                            }
                    ).toList()).build();
        }
    }

    public GroupTraining getGroupTrainingById(int groupTrainingId) throws NotFound {
        var groupTrainingOptional = groupTrainingRepository.findGroupTrainingById(groupTrainingId);
        if (groupTrainingOptional.isEmpty()) {
            throw new NotFound("Group training not found");
        }
        return groupTrainingOptional.get();
    }

    public GroupTrainingBooking getGroupTrainingBookingById(int groupTrainingId) throws NotFound {
        var groupTrainingBookingOptional = bookingRepository.findGroupTrainingBookingById(groupTrainingId);
        if (groupTrainingBookingOptional.isEmpty()) {
            throw new NotFound("Group training booking not found");
        }
        return groupTrainingBookingOptional.get();
    }

    public int getFreePlacesLeftCounter(GroupTraining groupTraining) {
        int amountClientsReserved = bookingRepository.
                countGroupTrainingBookingByGroupTraining_idAndBookingStatus(groupTraining.getId(), BookingStatus.SCHEDULED);
        return groupTraining.getMaxParticipants() - amountClientsReserved;
    }

    public boolean groupTrainingHasFreePlaces(GroupTraining groupTraining) {
        return getFreePlacesLeftCounter(groupTraining) > 0;
    }

    public OperationInfo bookGroupTraining(BookGroupTrainingDTO dto) {
        var client = userService.getCurrentUser();
        if (client == null || !client.getRole().equals(Role.CLIENT)) {
            throw new ProhibitedOperationException("Only clients can book group training");
        }
        var groupTraining = getGroupTrainingById(dto.getGroupTrainingId());
        if (!groupTrainingHasFreePlaces(groupTraining)) {
            throw new ProhibitedOperationException("Group training has no places left");
        }
        if(hasConflictInClientSchedule(client.getId(), groupTraining.getStartDateTime(), groupTraining.getEndDateTime())){
            throw new ProhibitedOperationException("Client has another group training at this time");
        }
        if(personalServiceService.hasConflictInClientSchedule(client.getId(), groupTraining.getStartDateTime(), groupTraining.getEndDateTime())){
            throw new ProhibitedOperationException("Client has personal training at this time");
        }
        bookingRepository.deleteGroupTrainingBookingByClient_IdAndGroupTraining_Id(client.getId(), dto.getGroupTrainingId());
        var booking = GroupTrainingBooking.builder().
                bookingStatus(BookingStatus.SCHEDULED).client(client)
                .groupTraining(groupTraining).build();
        bookingRepository.save(booking);
        notificationService.createNotification(
                NotificationDTO.builder().senderEmail("SYSTEM").receiverEmail(client.getEmail()).
                        message("You have booked training : "
                                + groupTraining.getDetails().getTitle()
                                + " it's pass code is: " + groupTraining.getPassCode())
                        .build()
        );
        return new OperationInfo(true, "booking saved");
    }

    public OperationInfo changeBookingStatus(ChangeBookingStatusDTO dto) {
        var trainer = userService.getCurrentUser();
        if (trainer == null || !trainer.getRole().equals(Role.TRAINER)) {
            throw new ProhibitedOperationException("Only trainers can change status of booking");
        }
        var booking = getGroupTrainingBookingById(dto.getGroupTrainingId());
        if (trainer.getId() != booking.getGroupTraining().getTrainer().getId()) {
            throw new ProhibitedOperationException("Trainers can only change the status of bookings on their own group trainings");
        }
        booking.setBookingStatus(BookingStatus.valueOf(dto.getBookingStatus()));
        bookingRepository.save(booking);
        return new OperationInfo(true, "Book canceled");
    }

    public PaginatedBookingDTO getAllBookingsOfCurrentClient(PageRequest pageable) {
        var client = userService.getCurrentUser();
        if (client == null || !client.getRole().equals(Role.CLIENT)) {
            throw new ProhibitedOperationException("Only clients can acquire their bookings");
        }
        var page = bookingRepository.findGroupTrainingBookingByClient_id(client.getId(), pageable);
        return PaginatedBookingDTO.builder().
                totalPages(page.getTotalPages()).
                bookings(page.get().map(GroupTrainingBooking::toDTO).toList()).
                build();
    }

    public List<GroupTrainingProgramDetailsDTO> getAllProgramDetails() {
        return groupTrainingProgramDetailsRepository.findAll().stream().
                map(GroupTrainingProgramDetails::toDTO).toList();
    }

    public IdDTO createGroupTrainingDetails(GroupTrainingProgramDetailsDTO dto) {
        var details = groupTrainingProgramDetailsRepository.save(GroupTrainingProgramDetails.builder().
                title(dto.getTitle()).
                description(dto.getDescription()).
                programDetails(dto.getProgramDetails()).build());
        return new IdDTO(details.getId());
    }

    public PaginatedGroupTrainingDTO getAllBookedGroupTrainingsOfCurrentClient(PageRequest pageable) {
        var client = userService.getCurrentUser();
        if (client == null || !client.getRole().equals(Role.CLIENT)) {
            throw new ProhibitedOperationException("Only clients can acquire their bookings");
        }
        var page = bookingRepository.findGroupTrainingBookingByClient_id(client.getId(), pageable);
        return PaginatedGroupTrainingDTO.builder()
                .totalPages(page.getTotalPages())
                .groupTrainings(page.get().map(GroupTrainingBooking::getGroupTraining).
                        map(GroupTraining::includePassCode).toList()).build();
    }
}
