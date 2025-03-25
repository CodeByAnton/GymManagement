package com.annton.api.data.repositories;

import com.annton.api.data.entities.PassCodeToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassCodeTokenRepository extends JpaRepository<PassCodeToken, Integer> {
    void deletePassCodeTokenByUser_id(int userId);
}
