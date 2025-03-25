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
public class GroupTrainingDTO {
    @Nullable
    private Integer id;
    @Nullable
    private Integer trainerId;
    private int duration;
    private int maxParticipants;
    private String startDateTime;
    private String endDateTime;
    @Nullable
    private String eventStatus;
    @Nullable
    private String passCode;

    private String programDetailsId;
    @Nullable
    private String title;
    @Nullable
    private String description;
    @Nullable
    private String programDetails;
    @Nullable
    private Boolean clientHasBooking;
}
