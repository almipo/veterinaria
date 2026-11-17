package com.veterinaria.auth.application.service;

import com.veterinaria.auth.adapter.in.web.dto.AuthResponse;
import com.veterinaria.auth.adapter.in.web.dto.LoginRequest;
import com.veterinaria.auth.adapter.in.web.dto.RegisterRequest;
import com.veterinaria.auth.domain.exception.CredencialesInvalidasException;
import com.veterinaria.auth.domain.exception.UsuarioYaExisteException;
import com.veterinaria.auth.domain.model.Usuario;
import com.veterinaria.auth.domain.port.in.AuthService;
import com.veterinaria.auth.domain.port.out.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UsuarioRepositoryPort repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UsuarioRepositoryPort repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (repository.findByUsername(request.username()).isPresent()) {
            throw new UsuarioYaExisteException("El nombre de usuario ya existe");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol(request.rol());
        usuario = repository.save(usuario);
        return new AuthResponse(jwtService.generateToken(usuario.getUsername(), usuario.getRol()));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = repository.findByUsername(request.username())
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales inválidas"));
        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }
        return new AuthResponse(jwtService.generateToken(usuario.getUsername(), usuario.getRol()));
    }
}
