package com.annton.api.data.entities;

import com.annton.api.data.enums.BookingStatus;
import com.annton.api.dto.BookingDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "group_training_booking")
public class GroupTrainingBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "group_training_id", nullable = false)
    private GroupTraining groupTraining;

    @Column(name = "booking_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public BookingDTO toDTO(){
        return BookingDTO.builder().
                id(id).
                groupTrainingId(groupTraining.getId())
                .createdAt(createdAt.toString())
                .updatedAt(updatedAt.toString())
                .bookingStatus(bookingStatus.name())
                .build();
    }
}

