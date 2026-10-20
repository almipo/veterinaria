package com.veterinaria.mascota.adapter.out.persistence;

import com.veterinaria.mascota.domain.model.Mascota;
import com.veterinaria.mascota.domain.port.out.MascotaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MascotaRepository implements MascotaRepositoryPort {

    private final MascotaJpaRepository jpaRepository;

    @Override
    public List<Mascota> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Mascota> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Mascota> findByDuenioId(Long duenioId) {
        return jpaRepository.findByDuenioId(duenioId);
    }

    @Override
    public Mascota save(Mascota mascota) {
        return jpaRepository.save(mascota);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
