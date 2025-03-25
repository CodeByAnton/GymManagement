package com.annton.api.services;

import com.annton.api.data.entities.RoleApplication;
import com.annton.api.data.enums.ApplicationStatus;
import com.annton.api.data.enums.Role;
import com.annton.api.data.repositories.RoleApplicationRepository;
import com.annton.api.dto.*;
import com.annton.api.services.exceptions.NotFound;
import com.annton.api.services.exceptions.RoleMismatchException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final UserService userService;
    private final RoleApplicationRepository repository;
    private final OtpService otpService;

    public OperationInfo postNewApplication(PostApplicationDTO dto) throws MessagingException {

        var registerRequest = RegisterRequest.builder().
                email(dto.getEmail()).
                password(dto.getPassword()).
                name(dto.getName()).
                surname(dto.getSurname()).
                birthDate(dto.getBirthDate()).
                build();
        var user = userService.registerUser(registerRequest);
        var application = RoleApplication.builder().user(user).
                desiredRole(Role.valueOf(dto.getDesiredRole())).
                status(ApplicationStatus.CONSIDERATION).build();
        repository.save(application);
        otpService.sendOtpToken(dto.getEmail());
        return new OperationInfo(true, "Application created");
    }


    public OperationInfo considerApplication(ConsiderApplicationDTO considerApplicationDTO)
            throws RoleMismatchException
    {
        if(!userService.getCurrentUser().getRole().equals(Role.ADMINISTRATOR)){
            throw new RoleMismatchException("Not enough rights fot this action");
        }
        var applicationOptional = repository.
                findById(considerApplicationDTO.getApplicationId());
        var application = applicationOptional.orElseThrow(() -> new NotFound("Application not found"));
        var status = ApplicationStatus.valueOf(considerApplicationDTO.getNewStatus());
        application.setStatus(status);
        application.setConsideredAt(LocalDateTime.now());

        if(status.equals(ApplicationStatus.ACCEPTED)){
            var user = application.getUser();
            user.setRole(application.getDesiredRole());
            userService.save(user);
        }
        repository.save(application);
        return new OperationInfo(true, "Application status changed");
    }

    public List<ApplicationDTO> getApplications() {
        if(!userService.getCurrentUser().getRole().equals(Role.ADMINISTRATOR)){
            throw new RoleMismatchException("Not enough rights fot this action");
        }
        return repository.
                findRoleApplicationByStatus(ApplicationStatus.CONSIDERATION).
                stream().map(RoleApplication::toDTO).toList();
    }

}
