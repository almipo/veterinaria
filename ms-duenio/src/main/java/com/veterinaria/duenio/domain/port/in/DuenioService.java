package com.veterinaria.duenio.domain.port.in;

import com.veterinaria.duenio.domain.model.Duenio;

import java.util.List;

public interface DuenioService {
    List<Duenio> findAll();
    Duenio findById(Long id);
    Duenio create(Duenio duenio);
    Duenio update(Long id, Duenio duenio);
    void deleteById(Long id);
    Duenio findByEmail(String email);
    List<Duenio> findByNombre(String nombre);
    List<Duenio> findByApellido(String apellido);
    List<Duenio> findByNombreAndApellido(String nombre, String apellido);
}
