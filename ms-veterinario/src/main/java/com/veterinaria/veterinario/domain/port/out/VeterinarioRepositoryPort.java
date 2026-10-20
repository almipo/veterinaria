package com.veterinaria.veterinario.domain.port.out;

import com.veterinaria.veterinario.domain.model.Especialidad;
import com.veterinaria.veterinario.domain.model.Veterinario;

import java.util.List;
import java.util.Optional;

public interface VeterinarioRepositoryPort {
    List<Veterinario> findAll();
    Optional<Veterinario> findById(Long id);
    List<Veterinario> findByEspecialidad(Especialidad especialidad);
    Veterinario save(Veterinario veterinario);
    void deleteById(Long id);
    boolean existsByMatricula(String matricula);
}
