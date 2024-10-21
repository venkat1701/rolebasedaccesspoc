package io.github.venkat1701.rolebasedaccesscontrolpoc.controllers;

import io.github.venkat1701.rolebasedaccesscontrolpoc.dto.UserRegistrationDTO;
import io.github.venkat1701.rolebasedaccesscontrolpoc.model.User;
import io.github.venkat1701.rolebasedaccesscontrolpoc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admins")
@RestController
public class AdminController {
    private final UserService userService;
    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> createAdministrator(@RequestBody UserRegistrationDTO userDto) {
        User createdAdmin = userService.createAdministrator(userDto);

        return ResponseEntity.ok(createdAdmin);
    }
}
