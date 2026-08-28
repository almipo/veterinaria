package com.vet_system.Service;

import com.vet_system.DTO.TurnoRequestDTO;
import com.vet_system.DTO.TurnoResponseDTO;
import com.vet_system.Entity.EstadoTurno;
import com.vet_system.Entity.Mascota;
import com.vet_system.Entity.Turno;
import com.vet_system.Entity.Veterinario;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Mapper.TurnoMapper;
import com.vet_system.Repository.MascotaRepository;
import com.vet_system.Repository.TurnoRepository;
import com.vet_system.Repository.VeterinarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final TurnoMapper turnoMapper;

    public List<TurnoResponseDTO> getAllTurnos() {
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper::toDTO)
                .toList();
    }

    public TurnoResponseDTO getTurnoById(Long id) {

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Turno", id));

        return turnoMapper.toDTO(turno);
    }

    public List<TurnoResponseDTO> getTurnosByVeterinarioYFecha(
            Long veterinarioId,
            LocalDate fecha) {

        return turnoRepository
                .findByVeterinarioIdAndFecha(veterinarioId, fecha)
                .stream()
                .map(turnoMapper::toDTO)
                .toList();
    }

    @Transactional
    public TurnoResponseDTO createTurno(TurnoRequestDTO request) {

        Mascota mascota = mascotaRepository
                .findById(request.getMascotaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Mascota",
                                request.getMascotaId()
                        ));

        Veterinario veterinario = veterinarioRepository
                .findById(request.getVeterinarioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Veterinario",
                                request.getVeterinarioId()
                        ));

        boolean existeTurno =
                turnoRepository.existsByVeterinarioIdAndFechaAndHora(
                        request.getVeterinarioId(),
                        request.getFecha(),
                        request.getHora()
                );

        if (existeTurno) {
            throw new RuntimeException(
                    "El veterinario ya tiene un turno en ese horario"
            );
        }

        Turno turno = new Turno();

        turno.setFecha(request.getFecha());
        turno.setHora(request.getHora());
        turno.setMotivo(request.getMotivo());
        turno.setEstado(EstadoTurno.PENDIENTE);
        turno.setMascota(mascota);
        turno.setVeterinario(veterinario);

        Turno turnoGuardado = turnoRepository.save(turno);

        return turnoMapper.toDTO(turnoGuardado);
    }

    @Transactional
    public TurnoResponseDTO actualizarEstado(
            Long id,
            EstadoTurno nuevoEstado,
            String observaciones) {

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Turno", id));

        turno.setEstado(nuevoEstado);

        if (observaciones != null) {
            turno.setObservaciones(observaciones);
        }

        return turnoMapper.toDTO(
                turnoRepository.save(turno)
        );
    }
}