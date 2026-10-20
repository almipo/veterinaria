package com.veterinaria.veterinario.domain.port.in;

import com.veterinaria.veterinario.domain.model.Especialidad;
import com.veterinaria.veterinario.domain.model.Veterinario;

import java.util.List;

public interface VeterinarioService {
    List<Veterinario> findAll();
    Veterinario findById(Long id);
    List<Veterinario> findByEspecialidad(Especialidad especialidad);
    Veterinario create(Veterinario veterinario);
    Veterinario update(Long id, Veterinario veterinario);
    void deleteById(Long id);
}
