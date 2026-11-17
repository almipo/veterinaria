package com.veterinaria.auth.domain.port.out;
import com.veterinaria.auth.domain.model.Usuario;
import java.util.Optional;
public interface UsuarioRepositoryPort {
    Usuario save(Usuario usuario);

    Optional<Usuario> findByUsername(String username);

}
