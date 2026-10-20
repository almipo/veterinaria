package com.veterinaria.duenio.domain.port.out;

import com.veterinaria.duenio.domain.model.Duenio;

import java.util.List;
import java.util.Optional;

public interface DuenioRepositoryPort {
    List<Duenio> findAll();
    Optional<Duenio> findById(Long id);
    Duenio save(Duenio duenio);
    void deleteById(Long id);
    boolean existsByCedula(String cedula);
    boolean existsByEmail(String email);
    Optional<Duenio> findByEmail(String email);
    List<Duenio> findByNombre(String nombre);
    List<Duenio> findByApellido(String apellido);
    List<Duenio> findByNombreAndApellido(String nombre, String apellido);
}
