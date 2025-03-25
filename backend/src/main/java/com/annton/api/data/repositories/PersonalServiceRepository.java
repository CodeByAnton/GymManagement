package com.annton.api.data.repositories;

import com.annton.api.data.entities.PersonalService;
import com.annton.api.data.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PersonalServiceRepository extends JpaRepository<PersonalService, Integer> {
    Page<PersonalService> findByClient_IdAndStatus(Integer clientId, Pageable pageable, EventStatus status);
    Page<PersonalService> findByTrainer_IdAndStatus(Integer trainerId, Pageable pageable, EventStatus status);

    Optional<PersonalService> findPersonalServiceById(int id);

    @Query("SELECT ps FROM PersonalService ps " +
            "WHERE ps.trainer.id = :trainerId AND " +
            "ps.status != com.annton.api.data.enums.EventStatus.CANCELLED AND " + // Исключаем отмененные услуги
            "((ps.startDateTime <= :newStartTime AND ps.endDateTime > :newStartTime) OR " +
            "(ps.startDateTime < :newEndTime AND ps.endDateTime >= :newEndTime) OR " +
            "(ps.startDateTime >= :newStartTime AND ps.endDateTime <= :newEndTime))")
    List<PersonalService> findTrainerScheduleConflict(
            @Param("trainerId") int trainerId,
            @Param("newStartTime") LocalDateTime newStartTime,
            @Param("newEndTime") LocalDateTime newEndTime);

    @Query("SELECT ps FROM PersonalService ps " +
            "WHERE ps.client.id = :clientId AND " +
            "ps.status != com.annton.api.data.enums.EventStatus.CANCELLED AND " + // Исключаем отмененные услуги
            "((ps.startDateTime <= :newStartTime AND ps.endDateTime > :newStartTime) OR " +
            "(ps.startDateTime < :newEndTime AND ps.endDateTime >= :newEndTime) OR " +
            "(ps.startDateTime >= :newStartTime AND ps.endDateTime <= :newEndTime))")
    List<PersonalService> findClientScheduleConflict(
            @Param("clientId") int clientId,
            @Param("newStartTime") LocalDateTime newStartTime,
            @Param("newEndTime") LocalDateTime newEndTime);
}

