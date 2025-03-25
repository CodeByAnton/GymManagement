package com.annton.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeStatusGroupTrainingDTO {
    private int groupTrainingId;
    private String newEventStatus;
}
