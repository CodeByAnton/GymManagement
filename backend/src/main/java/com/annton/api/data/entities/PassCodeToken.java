package com.annton.api.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pass_code_token")
public class PassCodeToken {

    private final static int fiveMinutes = 1000 * 60 * 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Size(min = 6, max = 6)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Timestamp expiresAt;

    @PrePersist
    public void prePersist(){
        expiresAt = new Timestamp(System.currentTimeMillis()
                + fiveMinutes);
    }
}
