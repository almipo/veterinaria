package com.veterinaria.turno.application;

import com.veterinaria.turno.domain.exception.MascotaNotFoundException;
import com.veterinaria.turno.domain.exception.TurnoNotFoundException;
import com.veterinaria.turno.domain.exception.TurnoSuperpuestoException;
import com.veterinaria.turno.domain.exception.VeterinarioNotFoundException;
import com.veterinaria.turno.domain.model.EstadoTurno;
import com.veterinaria.turno.domain.model.Turno;
import com.veterinaria.turno.domain.port.in.TurnoService;
import com.veterinaria.turno.domain.port.out.MascotaServicePort;
import com.veterinaria.turno.domain.port.out.TurnoRepositoryPort;
import com.veterinaria.turno.domain.port.out.VeterinarioServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepositoryPort turnoRepository;
    private final MascotaServicePort mascotaService;
    private final VeterinarioServicePort veterinarioService;

    @Override
    public List<Turno> findAll() {
        return turnoRepository.findAll();
    }

    @Override
    public Turno findById(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException(id));
    }

    @Override
    public List<Turno> findByVeterinarioId(Long veterinarioId) {
        return turnoRepository.findByVeterinarioId(veterinarioId);
    }

    @Override
    public List<Turno> findByMascotaId(Long mascotaId) {
        return turnoRepository.findByMascotaId(mascotaId);
    }

    @Override
    public List<Turno> findAgenda(Long veterinarioId, LocalDate fecha) {
        LocalDateTime desde = fecha.atStartOfDay();
        LocalDateTime hasta = fecha.plusDays(1).atStartOfDay();
        return turnoRepository.findByVeterinarioIdAndFechaHoraBetween(veterinarioId, desde, hasta);
    }

    @Override
    @Transactional
    public Turno create(Turno turno) {
        if (!mascotaService.existsById(turno.getMascotaId())) {
            throw new MascotaNotFoundException(turno.getMascotaId());
        }

        if (!veterinarioService.existsById(turno.getVeterinarioId())) {
            throw new VeterinarioNotFoundException(turno.getVeterinarioId());
        }

        if (turnoRepository.existsByVeterinarioIdAndFechaHora(
                turno.getVeterinarioId(), turno.getFechaHora())) {
            throw new TurnoSuperpuestoException(
                    "El veterinario ya tiene un turno en ese horario"
            );
        }

        turno.setId(null);
        turno.setEstado(EstadoTurno.PENDIENTE);
        return turnoRepository.save(turno);
    }

    @Override
    @Transactional
    public Turno updateStatus(Long id, EstadoTurno estado, String observaciones) {
        Turno turno = findById(id);
        turno.setEstado(estado);
        if (observaciones != null) {
            turno.setObservaciones(observaciones);
        }
        return turnoRepository.save(turno);
    }

    @Override
    @Transactional
    public Turno cancel(Long id) {
        return updateStatus(id, EstadoTurno.CANCELADO, null);
    }
}
