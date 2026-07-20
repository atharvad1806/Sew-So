package com.athdhup.sewnso.dto;

public record LoginRequest(
    String email,
    String password
) {}