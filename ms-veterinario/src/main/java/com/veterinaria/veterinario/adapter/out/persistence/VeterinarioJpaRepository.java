package com.veterinaria.veterinario.adapter.out.persistence;

import com.veterinaria.veterinario.domain.model.Especialidad;
import com.veterinaria.veterinario.domain.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeterinarioJpaRepository extends JpaRepository<Veterinario, Long> {
    List<Veterinario> findByEspecialidad(Especialidad especialidad);
    boolean existsByMatricula(String matricula);
}
