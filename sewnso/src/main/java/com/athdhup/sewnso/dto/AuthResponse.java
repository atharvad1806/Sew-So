package com.athdhup.sewnso.dto;

public record AuthResponse(
    Long userId,
    String email,
    String role,
    String token
) {}
