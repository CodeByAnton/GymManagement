package com.annton.api.data.entities;

import com.annton.api.data.enums.ApplicationStatus;
import com.annton.api.data.enums.Role;
import com.annton.api.dto.ApplicationDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "role_application")
public class RoleApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Status type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    private Role desiredRole;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, name="created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name="considered_at")
    private LocalDateTime consideredAt;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }

    public ApplicationDTO toDTO() {
        return ApplicationDTO.builder()
                .id(id)
                .email(user.getEmail())
                .name(user.getUserDetails().getName())
                .surname(user.getUserDetails().getSurname())
                .desiredRole(desiredRole.name())
                .build();
    }

}
