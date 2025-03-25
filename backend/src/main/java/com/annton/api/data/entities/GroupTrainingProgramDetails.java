package com.annton.api.data.entities;

import com.annton.api.dto.GroupTrainingProgramDetailsDTO;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "group_training_program_details")
public class GroupTrainingProgramDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "program_details", nullable = false, columnDefinition = "TEXT")
    private String programDetails;

    public GroupTrainingProgramDetailsDTO toDTO(){
        return GroupTrainingProgramDetailsDTO.builder()
                .id(id)
                .title(title)
                .description(description)
                .programDetails(programDetails)
                .build();
    }
}
