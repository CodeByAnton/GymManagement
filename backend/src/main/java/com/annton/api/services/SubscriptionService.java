package com.annton.api.services;


import com.annton.api.data.entities.Subscription;
import com.annton.api.data.entities.User;
import com.annton.api.data.enums.Role;
import com.annton.api.data.repositories.SubscriptionRepository;
import com.annton.api.dto.DTOWrapper;
import com.annton.api.dto.OperationInfo;
import com.annton.api.dto.SubscriptionDTO;
import com.annton.api.services.exceptions.NotFound;
import com.annton.api.services.exceptions.ProhibitedOperationException;
import com.annton.api.services.exceptions.RoleMismatchException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    @Transactional
    public OperationInfo createSubscription(SubscriptionDTO dto)
            throws NotFound, RoleMismatchException, ProhibitedOperationException {

        var client = userService.getUserByEmail(dto.getEmail());

        if (!client.getRole().equals(Role.CLIENT)) {
            throw new RoleMismatchException("Provided client id is incorrect");
        }

        var creator = userService.getCurrentUser();

        if(creator == null || !creator.getRole().equals(Role.ADMINISTRATOR)){
            throw new ProhibitedOperationException("Only administrators are able to create subscriptions");
        }
        subscriptionRepository.deleteByClient_Id(client.getId());
        subscriptionRepository.flush();
        var subscription=Subscription.builder()
                .client(client)
                .purchaseDate(LocalDate.from(LocalDateTime.now()))
                .startDate(LocalDate.parse(dto.getStartDate()))
                .endDate(LocalDate.parse(dto.getEndDate()))
                .price(dto.getPrice())
                .build();
        subscriptionRepository.save(subscription);
        return new OperationInfo(true, "subscription created");
    }

    public DTOWrapper<SubscriptionDTO> getSubscriptionOfCurrentUser() {
        var client = userService.getCurrentUser();
        return getSubscriptionOfUser(client);
    }

    public DTOWrapper<SubscriptionDTO> getSubscriptionOfUserByEmail(String email) {
        var client = userService.getUserByEmail(email);
        return getSubscriptionOfUser(client);
    }

    private DTOWrapper<SubscriptionDTO> getSubscriptionOfUser(User client){
        if (!client.getRole().equals(Role.CLIENT)) {
            throw new RoleMismatchException("Provided client id is incorrect");
        }
        var optionalSubscription = subscriptionRepository.findByClient_Id(client.getId());
        if(optionalSubscription.isEmpty()){
            return new DTOWrapper<>(false, null);
        }
        var subscription = optionalSubscription.get();

        if(subscription.getEndDate().isBefore(LocalDate.now())){
            return new DTOWrapper<>(false, null);
        }
        return new DTOWrapper<>(true, subscription.toDTO());
    }
}
