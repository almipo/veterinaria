package com.vet_system.Controller;

import com.vet_system.DTO.TurnoRequestDTO;
import com.vet_system.DTO.TurnoResponseDTO;
import com.vet_system.Entity.Duenio;
import com.vet_system.Entity.EstadoTurno;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.DuenioService;
import com.vet_system.Service.TurnoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/turno")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> getAllTurnos() {
        return ResponseEntity.ok(
                turnoService.getAllTurnos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTurnoById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    turnoService.getTurnoById(id)
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/agenda")
    public ResponseEntity<List<TurnoResponseDTO>> getAgenda(
            @RequestParam Long veterinarioId,
            @RequestParam LocalDate fecha) {

        return ResponseEntity.ok(
                turnoService.getTurnosByVeterinarioYFecha(
                        veterinarioId,
                        fecha
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createTurno(
            @RequestBody TurnoRequestDTO request) {

        try {
            TurnoResponseDTO nuevo =
                    turnoService.createTurno(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevo);

        } catch (ResourceNotFoundException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoTurno estado,
            @RequestParam(required = false) String observaciones) {

        try {
            return ResponseEntity.ok(
                    turnoService.actualizarEstado(
                            id,
                            estado,
                            observaciones
                    )
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}