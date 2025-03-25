package com.annton.api.services;

import com.annton.api.data.entities.Notification;
import com.annton.api.data.enums.NotificationStatus;
import com.annton.api.data.repositories.NotificationRepository;
import com.annton.api.dto.IdDTO;
import com.annton.api.dto.NotificationDTO;
import com.annton.api.dto.OperationInfo;
import com.annton.api.dto.PaginatedNotificationDTO;
import com.annton.api.services.exceptions.NotFound;
import com.annton.api.services.exceptions.ProhibitedOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public PaginatedNotificationDTO getNotificationsOfCurrentUser(PageRequest pageable) {
        var notificationPage = notificationRepository.
                findByUser_email(userService.getCurrentUserEmail(), pageable);
        return PaginatedNotificationDTO.builder().
                notifications(notificationPage.
                        getContent().stream().
                        map(Notification::toDTO).toList()).
                totalPages(notificationPage.getTotalPages()).build();
    }

    public OperationInfo createNotification(NotificationDTO dto) {
        var notification = Notification.builder().
                status(NotificationStatus.UNCHECKED).
                user(userService.getUserByEmail(dto.getReceiverEmail())).
                sender(userService.getCurrentUser()).message(dto.getMessage())
                .notificationDate(Instant.now()).build();
        notificationRepository.save(notification);
        return new OperationInfo(true, "Notification created");
    }

    public OperationInfo checkNotification(IdDTO idDTO) throws ProhibitedOperationException {
        var notification = notificationRepository.getNotificationById(idDTO.getId()).
                orElseThrow(() -> new NotFound("Notification not found"));
        if (notification.getStatus().equals(NotificationStatus.CHECKED)) {
            throw new ProhibitedOperationException("Notification has been checked already");
        }
        var receiverEmail = notification.getUser().getEmail();
        if (!receiverEmail.equals(userService.getCurrentUserEmail())) {
            throw new ProhibitedOperationException("prohibited operation");
        }
        notification.setStatus(NotificationStatus.CHECKED);
        notificationRepository.flush();
        return new OperationInfo(true, "Notification checked");
    }

}
