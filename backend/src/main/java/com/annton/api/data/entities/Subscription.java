package com.annton.api.data.entities;

import com.annton.api.dto.SubscriptionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User client;

    private LocalDate purchaseDate;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    @Check(constraints = "price >= 0")
    private Integer price;


    public SubscriptionDTO toDTO() {
        return SubscriptionDTO.builder()
                .id(id)
                .email(client.getEmail())
                .purchaseDate(purchaseDate.toString())
                .startDate(startDate.toString())
                .endDate(endDate.toString())
                .price(price)
                .build();
    }
}

