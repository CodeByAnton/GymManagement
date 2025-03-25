package com.annton.api.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDTO {
    @Nullable
    private Integer id;

    private String email;

    @Nullable
    private String purchaseDate;

    private String startDate;

    private String endDate;

    private Integer price;

}
