package com.veterinaria.turno.adapter.in.web;

import com.veterinaria.turno.adapter.in.web.dto.TurnoMapper;
import com.veterinaria.turno.adapter.in.web.dto.TurnoRequest;
import com.veterinaria.turno.adapter.in.web.dto.TurnoResponse;
import com.veterinaria.turno.domain.model.EstadoTurno;
import com.veterinaria.turno.domain.model.Turno;
import com.veterinaria.turno.domain.port.in.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
@Tag(name = "Turnos", description = "Gestion de turnos veterinarios")
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping
    @Operation(summary = "Listar todos los turnos")
    public ResponseEntity<List<TurnoResponse>> findAll() {
        return ResponseEntity.ok(
                turnoService.findAll().stream()
                        .map(TurnoMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar turno por ID")
    public ResponseEntity<TurnoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(TurnoMapper.toResponse(turnoService.findById(id)));
    }

    @GetMapping("/veterinario/{veterinarioId}")
    @Operation(summary = "Buscar turnos por veterinario")
    public ResponseEntity<List<TurnoResponse>> findByVeterinarioId(
            @PathVariable Long veterinarioId) {
        return ResponseEntity.ok(
                turnoService.findByVeterinarioId(veterinarioId).stream()
                        .map(TurnoMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/mascota/{mascotaId}")
    @Operation(summary = "Buscar historial de turnos por mascota")
    public ResponseEntity<List<TurnoResponse>> findByMascotaId(
            @PathVariable Long mascotaId) {
        return ResponseEntity.ok(
                turnoService.findByMascotaId(mascotaId).stream()
                        .map(TurnoMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/agenda")
    @Operation(summary = "Consultar agenda de un veterinario por fecha")
    public ResponseEntity<List<TurnoResponse>> findAgenda(
            @RequestParam Long veterinarioId,
            @RequestParam LocalDate fecha) {
        return ResponseEntity.ok(
                turnoService.findAgenda(veterinarioId, fecha).stream()
                        .map(TurnoMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping
    @Operation(summary = "Crear turno validando mascota y veterinario con OpenFeign")
    public ResponseEntity<TurnoResponse> create(
            @Valid @RequestBody TurnoRequest request) {
        Turno creado = turnoService.create(TurnoMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TurnoMapper.toResponse(creado));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del turno")
    public ResponseEntity<TurnoResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam EstadoTurno estado,
            @RequestParam(required = false) String observaciones) {
        return ResponseEntity.ok(
                TurnoMapper.toResponse(turnoService.updateStatus(id, estado, observaciones))
        );
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar turno")
    public ResponseEntity<TurnoResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(TurnoMapper.toResponse(turnoService.cancel(id)));
    }
}
