package com.veterinaria.duenio.adapter.out.persistence;

import com.veterinaria.duenio.domain.model.Duenio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface DuenioJpaRepository extends JpaRepository<Duenio, Long> {
    boolean existsByCedula(String cedula);
    boolean existsByEmail(String email);
    Optional<Duenio> findByEmail(String email);
    List<Duenio> findByNombre(String nombre);
    List<Duenio> findByApellido(String apellido);
    List<Duenio> findByNombreAndApellido(String nombre, String apellido);
}
