package com.annton.api.data.repositories;

import com.annton.api.data.entities.GroupTrainingBooking;
import com.annton.api.data.enums.BookingStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupTrainingBookingRepository extends JpaRepository<GroupTrainingBooking, Integer> {
    Optional<GroupTrainingBooking> findGroupTrainingBookingById(int id);
    int countGroupTrainingBookingByGroupTraining_idAndBookingStatus(int groupTrainingId, BookingStatus bookingStatus);
    Page<GroupTrainingBooking> findGroupTrainingBookingByClient_id(int clientId, Pageable pageable);
    Optional<GroupTrainingBooking> findGroupTrainingBookingByClient_IdAndGroupTraining_Id(int clientId, int groupTrainingId);
    @Transactional
    void deleteGroupTrainingBookingByClient_IdAndGroupTraining_Id(int clientId, int groupTrainingId);
    @Query("SELECT gtb FROM GroupTrainingBooking gtb " +
            "JOIN gtb.groupTraining gt " +
            "WHERE gtb.client.id = :clientId AND " +
            "gtb.bookingStatus != com.annton.api.data.enums.BookingStatus.CANCELLED AND " +
            "((gt.startDateTime <= :newStartTime AND gt.endDateTime > :newStartTime) OR " +
            "(gt.startDateTime < :newEndTime AND gt.endDateTime >= :newEndTime) OR " +
            "(gt.startDateTime >= :newStartTime AND gt.endDateTime <= :newEndTime))")
    List<GroupTrainingBooking> findClientGroupTrainingScheduleConflict(
            @Param("clientId") int clientId,
            @Param("newStartTime") LocalDateTime newStartTime,
            @Param("newEndTime") LocalDateTime newEndTime);

    @Query("SELECT CASE WHEN COUNT(gtb) > 0 THEN true ELSE false END " +
            "FROM GroupTrainingBooking gtb " +
            "WHERE gtb.client.id = :clientId AND " +
            "gtb.groupTraining.id = :groupTrainingId AND " +
            "gtb.bookingStatus != com.annton.api.data.enums.BookingStatus.CANCELLED")
    boolean isClientBookedOnGroupTraining(
            @Param("clientId") int clientId,
            @Param("groupTrainingId") int groupTrainingId);

    @Modifying
    @Transactional
    @Query("UPDATE GroupTrainingBooking gtb " +
            "SET gtb.bookingStatus = com.annton.api.data.enums.BookingStatus.CANCELLED " +
            "WHERE gtb.client.id = :clientId AND " +
            "gtb.groupTraining.id = :groupTrainingId")
    void cancelBookingByClientAndGroupTraining(
            @Param("clientId") int clientId,
            @Param("groupTrainingId") int groupTrainingId);

}
