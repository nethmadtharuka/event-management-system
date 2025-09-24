package com.eventmanagement.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.backend.dto.AuthDtos;
import com.eventmanagement.backend.model.Role;
import com.eventmanagement.backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDtos.RegisterRequest request,
                                      @RequestParam(value = "role", required = false, defaultValue = "USER") Role role) {
        try {
            return ResponseEntity.ok(userService.register(request, role));
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            log.warn("Registration conflict username='{}' email='{}' cause='{}'", request.username, request.email, ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "Username or email already exists"));
        } catch (IllegalArgumentException ex) {
            log.warn("Registration validation username='{}' email='{}' msg='{}'", request.username, request.email, ex.getMessage());
            return ResponseEntity.badRequest().body(java.util.Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Registration failed username='{}' email='{}'", request.username, request.email, ex);
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Internal error during registration"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        try {
            return ResponseEntity.ok(userService.login(request));
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", "Invalid credentials"));
        }
    }
}


