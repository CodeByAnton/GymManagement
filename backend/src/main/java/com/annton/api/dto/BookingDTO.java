package com.annton.api.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    @Nullable
    private Integer id;
    private int groupTrainingId;
    private String updatedAt;
    private String createdAt;
    private String bookingStatus;
}
