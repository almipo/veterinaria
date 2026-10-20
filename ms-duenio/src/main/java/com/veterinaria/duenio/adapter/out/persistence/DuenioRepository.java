package com.veterinaria.duenio.adapter.out.persistence;

import com.veterinaria.duenio.domain.model.Duenio;
import com.veterinaria.duenio.domain.port.out.DuenioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DuenioRepository implements DuenioRepositoryPort {

    private final DuenioJpaRepository jpaRepository;

    @Override
    public List<Duenio> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Duenio> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Duenio save(Duenio duenio) {
        return jpaRepository.save(duenio);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCedula(String cedula) {
        return jpaRepository.existsByCedula(cedula);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<Duenio> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public List<Duenio> findByNombre(String nombre) {
        return jpaRepository.findByNombre(nombre);
    }

    @Override
    public List<Duenio> findByApellido(String apellido) {
        return jpaRepository.findByApellido(apellido);
    }

    @Override
    public List<Duenio> findByNombreAndApellido(String nombre, String apellido) {
        return jpaRepository.findByNombreAndApellido(nombre, apellido);
    }
}
