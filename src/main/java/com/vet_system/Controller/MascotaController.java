package com.vet_system.Controller;


import com.vet_system.DTO.MascotaDTO;
import com.vet_system.Entity.Mascota;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor

@Tag(name = "Mascotas", description = "Gestión de mascotas")
public class MascotaController {
    private final MascotaService mascotaService;

    @Operation(summary = "Listar todas las mascotas", description = "Obtiene una lista de todas las mascotas registradas en el sistema")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de mascotas obtenida correctamente"
    )
    @GetMapping
    public ResponseEntity<List<MascotaDTO>> listarTodas() {
        return ResponseEntity.ok(
                mascotaService.getAllMascotas()
        );
    }



    @Operation(summary = "Buscar mascota por ID", description = "Obtiene una mascota utilizando su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota encontrada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> buscarPorId(
            @Parameter(description = "ID de la mascota", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(
                mascotaService.getMascotaById(id)
        );
    }
    @Operation(summary = "Registrar una mascota", description = "Registra una nueva mascota en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mascota creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado"),
            @ApiResponse(responseCode = "409", description = "Mascota duplicada")
    })
    @PostMapping
    public ResponseEntity<MascotaDTO> registrarMascota(
            @Parameter(
                    description = "ID del dueño al que pertenece la mascota",
                    example = "1"
            )
            @RequestParam Long duenioId,
            @Valid @RequestBody MascotaDTO dto) {

        MascotaDTO nuevaMascota =
                mascotaService.createMascota(duenioId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaMascota);
    }
    @Operation(summary = "Actualizar una mascota", description = "Actualiza la información de una mascota existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MascotaDTO> actualizarMascota(   @Parameter(
                                                                       description = "ID de la mascota",
                                                                       example = "1"
                                                               )
            @PathVariable Long id,
            @Valid @RequestBody MascotaDTO dto) {

        return ResponseEntity.ok(
                mascotaService.updateMascota(id, dto)
        );
    }
    @Operation(summary = "Eliminar una mascota", description = "Elimina una mascota del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mascota eliminada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(@Parameter(
            description = "ID de la mascota",
            example = "1"
    )
            @PathVariable Long id) {

        mascotaService.deleteMascota(id);

        return ResponseEntity.noContent().build();
    }}