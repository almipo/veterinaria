package com.veterinaria.turno.adapter.out.persistence;

import com.veterinaria.turno.domain.model.Turno;
import com.veterinaria.turno.domain.port.out.TurnoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TurnoRepository implements TurnoRepositoryPort {

    private final TurnoJpaRepository jpaRepository;

    @Override
    public List<Turno> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Turno> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Turno> findByVeterinarioId(Long veterinarioId) {
        return jpaRepository.findByVeterinarioId(veterinarioId);
    }

    @Override
    public List<Turno> findByMascotaId(Long mascotaId) {
        return jpaRepository.findByMascotaIdOrderByFechaHoraDesc(mascotaId);
    }

    @Override
    public List<Turno> findByVeterinarioIdAndFechaHoraBetween(
            Long veterinarioId,
            LocalDateTime desde,
            LocalDateTime hasta) {
        return jpaRepository.findByVeterinarioIdAndFechaHoraBetween(veterinarioId, desde, hasta);
    }

    @Override
    public boolean existsByVeterinarioIdAndFechaHora(Long veterinarioId, LocalDateTime fechaHora) {
        return jpaRepository.existsByVeterinarioIdAndFechaHora(veterinarioId, fechaHora);
    }

    @Override
    public Turno save(Turno turno) {
        return jpaRepository.save(turno);
    }
}
