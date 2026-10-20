package com.veterinaria.mascota.adapter.out.persistence;

import com.veterinaria.mascota.domain.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface MascotaJpaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByDuenioId(Long duenioId);
}
