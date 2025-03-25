package com.annton.api.data.entities;

import com.annton.api.data.enums.NotificationStatus;
import com.annton.api.dto.NotificationDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "sended_by", nullable = false)
    private User sender;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "notification_date", nullable = false)
    private Instant notificationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    public NotificationDTO toDTO() {
        return NotificationDTO.builder().
                id(id).
                receiverEmail(user.getEmail()).
                message(message).
                senderEmail(sender.getEmail()).
                notificationDate(notificationDate.toString()).
                notificationStatus(status.toString()).
                build();
    }

}

