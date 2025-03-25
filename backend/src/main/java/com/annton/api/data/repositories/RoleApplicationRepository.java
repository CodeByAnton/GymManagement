package com.annton.api.data.repositories;

import com.annton.api.data.entities.RoleApplication;
import com.annton.api.data.entities.User;
import com.annton.api.data.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleApplicationRepository extends JpaRepository<RoleApplication, Integer> {
    Optional<RoleApplication> findByUser(User user);
    List<RoleApplication> findRoleApplicationByStatus(ApplicationStatus status);
}
