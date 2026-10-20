package com.veterinaria.mascota.domain.port.out;

import com.veterinaria.mascota.domain.model.Mascota;

import java.util.List;
import java.util.Optional;

public interface MascotaRepositoryPort {
    List<Mascota> findAll();
    Optional<Mascota> findById(Long id);
    List<Mascota> findByDuenioId(Long duenioId);
    Mascota save(Mascota mascota);
    void deleteById(Long id);
}
