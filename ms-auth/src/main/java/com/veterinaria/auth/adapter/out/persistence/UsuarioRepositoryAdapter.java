package com.veterinaria.auth.adapter.out.persistence;

import com.veterinaria.auth.domain.model.Usuario;
import com.veterinaria.auth.domain.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}