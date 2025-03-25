package com.annton.api.data.repositories;

import com.annton.api.data.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findByUser_email(String email, Pageable pageable);
    Optional<Notification> getNotificationById(int id);
}
