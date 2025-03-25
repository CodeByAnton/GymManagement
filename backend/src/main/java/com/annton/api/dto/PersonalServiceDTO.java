package com.annton.api.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalServiceDTO {
    @Nullable
    private Integer id;
    private String clientEmail;
    @Nullable
    private Integer trainerId;
    @Nullable
    private String trainerEmail;
    private String startDateTime;
    private String endDateTime;
    private String serviceType;
    private Integer duration;
    @Nullable
    private String status;
    @Nullable
    private String passCode;
}
