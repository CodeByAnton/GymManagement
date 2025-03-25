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
public class NotificationDTO {
    @Nullable
    private Integer id;
    @Nullable
    private String receiverEmail;
    @Nullable
    private String senderEmail;
    private String message;
    @Nullable
    private String notificationDate;
    @Nullable
    private String notificationStatus;
}
