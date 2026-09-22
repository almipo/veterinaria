package com.vet_system.Controller;
import com.vet_system.DTO.VeterinarioDTO;
import com.vet_system.Entity.Duenio;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.DuenioService;
import com.vet_system.Service.VeterinarioService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/veterinario")
@RequiredArgsConstructor

@Tag(
        name = "Veterinarios",
        description = "Operaciones para gestionar veterinarios"
)
public class VeterinarioController {
    private final VeterinarioService veterinarioService;



    @Operation(summary = "Listar todos los veterinarios", description = "Obtiene una lista de todos los veterinarios registrados en el sistema")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de veterinarios obtenida correctamente"
    )
    @GetMapping
    public ResponseEntity<List<VeterinarioDTO>> getAllVeterinarios() {
        return ResponseEntity.ok(
                veterinarioService.getAllVeterinarios()
        );
    }

    @Operation(summary = "Buscar veterinario por ID", description = "Obtiene un veterinario utilizando su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veterinario encontrado"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioDTO> getVeterinarioById(  @Parameter(
            description = "ID del veterinario",
            example = "1"
    )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                veterinarioService.getVeterinarioById(id)
        );
    }
    @Operation(summary = "Registrar un veterinario", description = "Registra un nuevo veterinario en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Veterinario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Matrícula duplicada")
    })
    @PostMapping
    public ResponseEntity<VeterinarioDTO> createVeterinario(
            @Valid @RequestBody VeterinarioDTO dto) {

        VeterinarioDTO nuevo =
                veterinarioService.createVeterinario(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevo);
    }
    @Operation(summary = "Actualizar veterinario", description = "Actualiza la información de un veterinario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veterinario actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioDTO> updateVeterinario(
            @Parameter(
                    description = "ID del veterinario",
                    example = "1"
            )
            @PathVariable Long id,
            @Valid @RequestBody VeterinarioDTO dto) {

        return ResponseEntity.ok(
                veterinarioService.updateVeterinario(id, dto)
        );
    }
    @Operation(summary = "Eliminar veterinario", description = "Elimina un veterinario del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Veterinario eliminado"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinario(    @Parameter(
            description = "ID del veterinario",
            example = "1"
    )
            @PathVariable Long id) {

        veterinarioService.deleteVeterinario(id);

        return ResponseEntity.noContent().build();
    }}