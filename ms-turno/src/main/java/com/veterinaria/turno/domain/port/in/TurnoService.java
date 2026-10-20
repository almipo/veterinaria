package com.veterinaria.turno.domain.port.in;

import com.veterinaria.turno.domain.model.EstadoTurno;
import com.veterinaria.turno.domain.model.Turno;

import java.time.LocalDate;
import java.util.List;

public interface TurnoService {
    List<Turno> findAll();
    Turno findById(Long id);
    List<Turno> findByVeterinarioId(Long veterinarioId);
    List<Turno> findByMascotaId(Long mascotaId);
    List<Turno> findAgenda(Long veterinarioId, LocalDate fecha);
    Turno create(Turno turno);
    Turno updateStatus(Long id, EstadoTurno estado, String observaciones);
    Turno cancel(Long id);
}
