package com.annton.api.controllers;

import com.annton.api.data.entities.User;
import com.annton.api.dto.UserInfoDTO;
import com.annton.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserInfoController {
    private static final Logger logger = LogManager.getLogger(UserInfoController.class);
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoDTO> getCurrentUserInfo(){
        logger.info("GET request to /api/v1/user endpoint accepted");
        return ResponseEntity.ok(userService.getCurrentUser().toDTO());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMINISTRATOR') || hasRole('TRAINER')")
    public ResponseEntity<List<UserInfoDTO>> getUserInfoByNameAndSurname(
            @RequestParam String name,
            @RequestParam String surname){
        return ResponseEntity.ok(userService.
                getUserByNameAndSurname(name, surname).stream().
                map(User::toDTO).toList());
    }

}
