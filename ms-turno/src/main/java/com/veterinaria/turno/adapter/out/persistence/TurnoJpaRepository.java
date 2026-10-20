package com.veterinaria.turno.adapter.out.persistence;

import com.veterinaria.turno.domain.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TurnoJpaRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByVeterinarioId(Long veterinarioId);
    List<Turno> findByMascotaIdOrderByFechaHoraDesc(Long mascotaId);
    List<Turno> findByVeterinarioIdAndFechaHoraBetween(
            Long veterinarioId,
            LocalDateTime desde,
            LocalDateTime hasta
    );
    boolean existsByVeterinarioIdAndFechaHora(Long veterinarioId, LocalDateTime fechaHora);
}
