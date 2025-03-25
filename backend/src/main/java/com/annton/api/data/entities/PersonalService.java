package com.annton.api.data.entities;

import com.annton.api.data.enums.EventStatus;
import com.annton.api.data.enums.ServiceType;
import com.annton.api.dto.PersonalServiceDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personal_service")
public class PersonalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "service_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(name = "pass_code", nullable = false)
    private String passCode;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    public PersonalServiceDTO toDTO() {
        return PersonalServiceDTO.builder()
                .id(id)
                .clientEmail(client.getEmail()).
                trainerEmail(trainer.getEmail())
                .trainerId(trainer.getId())
                .serviceType(serviceType.toString())
                .startDateTime(startDateTime.toString())
                .endDateTime(endDateTime.toString())
                .status(status.toString())
                .build();
    }

    public PersonalServiceDTO includePassCode() {
        var dto = toDTO();
        dto.setPassCode(passCode);
        return dto;
    }
}

