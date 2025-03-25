package com.annton.api.services;

import com.annton.api.data.entities.User;
import com.annton.api.data.entities.UserDetails;
import com.annton.api.data.enums.Role;
import com.annton.api.data.repositories.UserDetailsRepository;
import com.annton.api.data.repositories.UserRepository;
import com.annton.api.dto.LoginRequest;
import com.annton.api.dto.RegisterRequest;
import com.annton.api.services.exceptions.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder( @Lazy PasswordEncoder passwordEncoder ) {
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) throws NotFound {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFound("User not found")
        );
    }

    public User getUserById(Integer id) throws NotFound {
        return userRepository.getUserById(id).orElseThrow(
                () -> new NotFound("User not found")
        );
    }

    public List<User> getUserByNameAndSurname(String name, String surname){
        return userRepository.findUsersByNameAndSurnameIgnoreCase(name, surname);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User checkPassword(LoginRequest loginRequest) throws BadCredentialsException {
        var user = getUserByEmail(loginRequest.getEmail());
        var passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if ( ! passwordMatches) throw new BadCredentialsException("Wrong password");
        return user;
    }

    public User registerUser(RegisterRequest registerRequest) throws BadCredentialsException {
        var userBuilder = User.builder();
        var optionalUser = userRepository.findByEmail(registerRequest.getEmail());

        if(optionalUser.isPresent())
            throw new BadCredentialsException("User with this email already exists");

        userBuilder.role(Role.CLIENT);
        userBuilder.email(registerRequest.getEmail());
        userBuilder.password(passwordEncoder.encode(registerRequest.getPassword()));
        var user = userBuilder.build();


        var userDetails = UserDetails.builder().
                name(registerRequest.getName()).
                surname(registerRequest.getSurname()).
                birthDate(LocalDate.parse(registerRequest.getBirthDate()))
                .build();
        userDetails = userDetailsRepository.save(userDetails);
        user.setUserDetails(userDetails);
        userRepository.save(user);
        return user;
    }

    public String getCurrentUserEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) return null;
        var userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    public User getCurrentUser() {
        var email = getCurrentUserEmail();
        if(email == null) return null;
        return getUserByEmail(email);
    }
}
