package com.veterinaria.mascota.adapter.in.web;

import com.veterinaria.mascota.adapter.in.web.dto.MascotaMapper;
import com.veterinaria.mascota.adapter.in.web.dto.MascotaRequest;
import com.veterinaria.mascota.adapter.in.web.dto.MascotaResponse;
import com.veterinaria.mascota.domain.model.Mascota;
import com.veterinaria.mascota.domain.port.in.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
@Tag(name = "Mascotas", description = "Gestión de mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    @GetMapping
    @Operation(summary = "Listar todas las mascotas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    public ResponseEntity<List<MascotaResponse>> findAll() {
        List<MascotaResponse> response = mascotaService.findAll()
                .stream()
                .map(MascotaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mascota por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota encontrada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    public ResponseEntity<MascotaResponse> findById(
            @Parameter(description = "ID de la mascota", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(
                MascotaMapper.toResponse(mascotaService.findById(id))
        );
    }

    @GetMapping("/duenio/{duenioId}")
    @Operation(summary = "Buscar mascotas por dueño")
    public ResponseEntity<List<MascotaResponse>> findByDuenioId(
            @PathVariable Long duenioId) {
        return ResponseEntity.ok(
                mascotaService.findByDuenioId(duenioId)
                        .stream()
                        .map(MascotaMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping
    @Operation(summary = "Registrar una mascota")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mascota creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<MascotaResponse> create(
            @Valid @RequestBody MascotaRequest request) {
        Mascota creada = mascotaService.create(MascotaMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MascotaMapper.toResponse(creada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una mascota")
    public ResponseEntity<MascotaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequest request) {
        Mascota actualizada = mascotaService.update(id, MascotaMapper.toEntity(request));
        return ResponseEntity.ok(MascotaMapper.toResponse(actualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una mascota")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        mascotaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
