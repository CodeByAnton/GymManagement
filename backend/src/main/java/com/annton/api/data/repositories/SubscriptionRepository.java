package com.annton.api.data.repositories;

import com.annton.api.data.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Optional<Subscription> findByClient_Id(Integer clientId);
    void deleteByClient_Id(Integer clientId);
}
