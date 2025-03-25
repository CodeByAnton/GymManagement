package com.annton.api.data.repositories;

import com.annton.api.data.entities.OtpToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpToken, Integer> {
    Optional<OtpToken> findByUser_email(String user_email);
    @Transactional
    void deleteByUser_email(String user_email);
}
