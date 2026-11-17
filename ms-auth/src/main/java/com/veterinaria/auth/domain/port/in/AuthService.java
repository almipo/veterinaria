package com.veterinaria.auth.domain.port.in;

import com.veterinaria.auth.adapter.in.web.dto.AuthResponse;
import com.veterinaria.auth.adapter.in.web.dto.LoginRequest;
import com.veterinaria.auth.adapter.in.web.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}