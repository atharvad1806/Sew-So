package com.athdhup.sewnso.service;

import com.athdhup.sewnso.dto.AuthResponse;
import com.athdhup.sewnso.dto.LoginRequest;
import com.athdhup.sewnso.dto.RegisterRequest;
import com.athdhup.sewnso.model.AppUser;
import com.athdhup.sewnso.repository.AppUserRepository;
import com.athdhup.sewnso.security.AppUserDetails;
import com.athdhup.sewnso.security.JwtService;
import com.athdhup.sewnso.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (appUserRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already registered: " + request.email());
        }

        AppUser user = new AppUser();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(AppUser.Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());

        AppUser saved = appUserRepository.save(user);

        String token = jwtService.generateToken(new AppUserDetails(saved));

        return new AuthResponse(saved.getId(), saved.getEmail(), saved.getRole().name(), token);
    }

    public AuthResponse login(LoginRequest request) {
        AppUser user = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(new AppUserDetails(user));

        return new AuthResponse(user.getId(), user.getEmail(), user.getRole().name(), token);
    }
}