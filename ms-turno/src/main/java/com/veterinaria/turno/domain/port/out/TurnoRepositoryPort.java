package com.veterinaria.turno.domain.port.out;

import com.veterinaria.turno.domain.model.Turno;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TurnoRepositoryPort {
    List<Turno> findAll();
    Optional<Turno> findById(Long id);
    List<Turno> findByVeterinarioId(Long veterinarioId);
    List<Turno> findByMascotaId(Long mascotaId);
    List<Turno> findByVeterinarioIdAndFechaHoraBetween(
            Long veterinarioId,
            LocalDateTime desde,
            LocalDateTime hasta
    );
    boolean existsByVeterinarioIdAndFechaHora(Long veterinarioId, LocalDateTime fechaHora);
    Turno save(Turno turno);
}
