package com.annton.api.dto;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupTrainingProgramDetailsDTO {
    @Id
    @Nullable
    private Integer id;

    private String title;

    private String description;

    private String programDetails;
}
