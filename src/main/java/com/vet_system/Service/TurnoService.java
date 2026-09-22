package com.vet_system.Service;

import com.vet_system.DTO.MedicamentoResponseDTO;
import com.vet_system.DTO.TurnoRequestDTO;
import com.vet_system.DTO.TurnoResponseDTO;
import com.vet_system.Entity.*;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Exception.StockInsuficienteException;
import com.vet_system.Exception.TurnoSuperpuestoException;
import com.vet_system.Mapper.MedicamentoMapper;
import com.vet_system.Mapper.TurnoMapper;
import com.vet_system.Repository.MascotaRepository;
import com.vet_system.Repository.MedicamentoRepository;
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
    private final MedicamentoRepository medicamentoRepository;
    private final MedicamentoMapper medicamentoMapper;

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

        turnoRepository.findByVeterinarioIdAndFechaAndHora(
                request.getVeterinarioId(),
                request.getFecha(),
                request.getHora()
        ).ifPresent(turnoConflictivo -> {
            throw new TurnoSuperpuestoException(
                    "El veterinario ya tiene el turno ID "
                            + turnoConflictivo.getId()
                            + " en el horario "
                            + turnoConflictivo.getFecha()
                            + " "
                            + turnoConflictivo.getHora()
            );
        });

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

    public List<MedicamentoResponseDTO> getMedicamentosByTurno(Long turnoId) {

        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Turno", turnoId));

        return turno.getMedicamentos()
                .stream()
                .map(medicamentoMapper::toDTO)
                .toList();
    }

    @Transactional
    public MedicamentoResponseDTO agregarMedicamento(
            Long turnoId,
            Long medicamentoId) {

        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Turno", turnoId));

        Medicamento medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicamento", medicamentoId));

        if (medicamento.getStock() <= 0) {
            throw new StockInsuficienteException(
                    "El medicamento " + medicamento.getNombre() + " no tiene stock disponible"
            );
        }

        turno.getMedicamentos().add(medicamento);

        medicamento.setStock(medicamento.getStock() - 1);

        medicamentoRepository.save(medicamento);
        turnoRepository.save(turno);

        return medicamentoMapper.toDTO(medicamento);
    }

}