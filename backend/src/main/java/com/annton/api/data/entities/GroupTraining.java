package com.annton.api.data.entities;

import com.annton.api.data.enums.EventStatus;
import com.annton.api.dto.GroupTrainingDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "group_training")
public class GroupTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "pass_code", nullable = false)
    private String passCode;

    @ManyToOne
    @JoinColumn(name = "details_id", nullable = false)
    private GroupTrainingProgramDetails details;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EventStatus status;

    public GroupTrainingDTO toDTO() {
        return GroupTrainingDTO.builder()
                .id(this.id != 0 ? this.id : null)
                .trainerId(this.trainer.getId())
                .duration((int) Duration.between(startDateTime, endDateTime).toMinutes())
                .maxParticipants(this.maxParticipants)
                .startDateTime(this.startDateTime.toString())
                .endDateTime(this.endDateTime.toString())
                .eventStatus(this.status.name())
                .title(this.details.getTitle())
                .description(this.details.getDescription())
                .programDetails(this.details.getProgramDetails())
                .build();
    }

    public GroupTrainingDTO includePassCode() {
        var dto = toDTO();
        dto.setPassCode(passCode);
        return dto;
    }

    public GroupTrainingDTO includeClientHasBooking(boolean flag) {
        var dto = toDTO();
        dto.setClientHasBooking(flag);
        return dto;
    }

}

