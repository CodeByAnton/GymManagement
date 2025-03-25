package com.annton.api.data.repositories;

import com.annton.api.data.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByUser_Id(int userId);

    Optional<RefreshToken> findByToken(String token);
}
