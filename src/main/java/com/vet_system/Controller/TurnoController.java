package com.vet_system.Controller;

import com.vet_system.DTO.MedicamentoResponseDTO;
import com.vet_system.DTO.TurnoRequestDTO;
import com.vet_system.DTO.TurnoResponseDTO;
import com.vet_system.Entity.Duenio;
import com.vet_system.Entity.EstadoTurno;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.DuenioService;
import com.vet_system.Service.TurnoService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/turno")
@RequiredArgsConstructor

@Tag(
        name = "Turnos",
        description = "Gestión de turnos veterinarios"
)
public class TurnoController {

    private final TurnoService turnoService;



    @Operation(summary = "Listar todos los turnos", description = "Obtiene una lista de todos los turnos registrados en el sistema")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de turnos obtenida correctamente"
    )
    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> getAllTurnos() {

        return ResponseEntity.ok(
                turnoService.getAllTurnos()
        );
    }


    @Operation(summary = "Buscar turno por ID",
            description = "Obtiene un turno utilizando su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> getTurnoById(

            @Parameter(
                    description = "ID del turno",
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                turnoService.getTurnoById(id)
        );
    }


    @Operation(
            summary = "Consultar agenda",
            description = "Obtiene los turnos de un veterinario para una fecha determinada"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Agenda obtenida correctamente"
    )
    @GetMapping("/agenda")
    public ResponseEntity<List<TurnoResponseDTO>> getAgenda(

            @Parameter(
                    description = "ID del veterinario",
                    example = "1"
            )
            @RequestParam Long veterinarioId,

            @Parameter(
                    description = "Fecha de la agenda",
                    example = "2026-10-10"
            )
            @RequestParam LocalDate fecha) {

        return ResponseEntity.ok(
                turnoService.getTurnosByVeterinarioYFecha(
                        veterinarioId,
                        fecha
                )
        );
    }


    @Operation(summary = "Crear un turno",
            description = "Registra un nuevo turno asociando una mascota con un veterinario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Turno creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Mascota o veterinario no encontrado"),
            @ApiResponse(responseCode = "409", description = "El veterinario ya tiene un turno en ese horario")
    })
    @PostMapping
    public ResponseEntity<TurnoResponseDTO> createTurno(
            @Valid @RequestBody TurnoRequestDTO request) {

        TurnoResponseDTO nuevo =
                turnoService.createTurno(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevo);
    }


    @Operation(
            summary = "Actualizar estado del turno",
            description = "Permite cambiar el estado y agregar observaciones"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TurnoResponseDTO> actualizarEstado(

            @Parameter(description = "ID del turno", example = "1")
            @PathVariable Long id,

            @Parameter(description = "Nuevo estado del turno")
            @RequestParam EstadoTurno estado,

            @Parameter(description = "Observaciones opcionales")
            @RequestParam(required = false) String observaciones) {

        return ResponseEntity.ok(
                turnoService.actualizarEstado(
                        id,
                        estado,
                        observaciones
                )
        );



    }


    @GetMapping("/{id}/medicamentos")
    public ResponseEntity<List<MedicamentoResponseDTO>> getMedicamentosByTurno(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                turnoService.getMedicamentosByTurno(id)
        );
    }
    @PostMapping("/{turnoId}/medicamentos/{medicamentoId}")
    public ResponseEntity<MedicamentoResponseDTO> agregarMedicamento(
            @PathVariable Long turnoId,
            @PathVariable Long medicamentoId) {

        return ResponseEntity.ok(
                turnoService.agregarMedicamento(turnoId, medicamentoId)
        );
    }
}