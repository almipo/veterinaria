package com.veterinaria.veterinario.adapter.out.persistence;

import com.veterinaria.veterinario.domain.model.Especialidad;
import com.veterinaria.veterinario.domain.model.Veterinario;
import com.veterinaria.veterinario.domain.port.out.VeterinarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VeterinarioRepository implements VeterinarioRepositoryPort {

    private final VeterinarioJpaRepository jpaRepository;

    @Override
    public List<Veterinario> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Veterinario> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Veterinario> findByEspecialidad(Especialidad especialidad) {
        return jpaRepository.findByEspecialidad(especialidad);
    }

    @Override
    public Veterinario save(Veterinario veterinario) {
        return jpaRepository.save(veterinario);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByMatricula(String matricula) {
        return jpaRepository.existsByMatricula(matricula);
    }
}
