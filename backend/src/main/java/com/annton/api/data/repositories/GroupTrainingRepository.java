package com.annton.api.data.repositories;

import com.annton.api.data.entities.GroupTraining;
import com.annton.api.data.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupTrainingRepository extends JpaRepository<GroupTraining, Integer> {
    Optional<GroupTraining> findGroupTrainingById(int id);
    Page<GroupTraining> findByTrainer_idAndStatus(int trainerId, EventStatus status, Pageable pageable);
    @Query("SELECT gt FROM GroupTraining gt " +
            "WHERE gt.trainer.id = :trainerId AND " +
            "gt.status != com.annton.api.data.enums.EventStatus.CANCELLED AND " +
            "((gt.startDateTime <= :newStartTime AND gt.endDateTime > :newStartTime) OR " +
            "(gt.startDateTime < :newEndTime AND gt.endDateTime >= :newEndTime) OR " +
            "(gt.startDateTime >= :newStartTime AND gt.endDateTime <= :newEndTime))")
    List<GroupTraining> findTrainerGroupTrainingScheduleConflict(
            @Param("trainerId") int trainerId,
            @Param("newStartTime") LocalDateTime newStartTime,
            @Param("newEndTime") LocalDateTime newEndTime);

    @Query("SELECT gt FROM GroupTraining gt " +
            "WHERE gt.startDateTime >= :startTime AND " +
            "gt.status != com.annton.api.data.enums.EventStatus.CANCELLED")
    Page<GroupTraining> findAllByStartTimeAfterAndNotCancelled(
            @Param("startTime") LocalDateTime startTime,
            Pageable pageable);
}
