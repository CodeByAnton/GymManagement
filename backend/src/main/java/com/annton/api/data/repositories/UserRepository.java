package com.annton.api.data.repositories;

import com.annton.api.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> getUserById(Integer id);
    @Query("SELECT u FROM User u JOIN u.userDetails ud WHERE LOWER(ud.name) = LOWER(:name) AND LOWER(ud.surname) = LOWER(:surname)")
    List<User> findUsersByNameAndSurnameIgnoreCase(@Param("name") String name, @Param("surname") String surname);
}
