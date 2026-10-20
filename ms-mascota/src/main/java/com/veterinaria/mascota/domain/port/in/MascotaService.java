package com.veterinaria.mascota.domain.port.in;

import com.veterinaria.mascota.domain.model.Mascota;

import java.util.List;

public interface MascotaService {
    List<Mascota> findAll();
    Mascota findById(Long id);
    List<Mascota> findByDuenioId(Long duenioId);
    Mascota create(Mascota mascota);
    Mascota update(Long id, Mascota mascota);
    void deleteById(Long id);
}
