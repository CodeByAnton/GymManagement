package com.annton.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginatedGroupTrainingDTO {
    private List<GroupTrainingDTO> groupTrainings;
    private int totalPages;
}
