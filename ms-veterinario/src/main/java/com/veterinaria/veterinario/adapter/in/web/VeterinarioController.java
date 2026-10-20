package com.veterinaria.veterinario.adapter.in.web;

import com.veterinaria.veterinario.adapter.in.web.dto.VeterinarioMapper;
import com.veterinaria.veterinario.adapter.in.web.dto.VeterinarioRequest;
import com.veterinaria.veterinario.adapter.in.web.dto.VeterinarioResponse;
import com.veterinaria.veterinario.domain.model.Especialidad;
import com.veterinaria.veterinario.domain.model.Veterinario;
import com.veterinaria.veterinario.domain.port.in.VeterinarioService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
@Tag(name = "Veterinarios", description = "Gestion de veterinarios")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    @Operation(summary = "Listar todos los veterinarios")
    public ResponseEntity<List<VeterinarioResponse>> findAll() {
        return ResponseEntity.ok(
                veterinarioService.findAll().stream()
                        .map(VeterinarioMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veterinario por ID")
    public ResponseEntity<VeterinarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                VeterinarioMapper.toResponse(veterinarioService.findById(id))
        );
    }

    @GetMapping("/especialidad/{especialidad}")
    @Operation(summary = "Buscar veterinarios por especialidad")
    public ResponseEntity<List<VeterinarioResponse>> findByEspecialidad(
            @PathVariable Especialidad especialidad) {
        return ResponseEntity.ok(
                veterinarioService.findByEspecialidad(especialidad).stream()
                        .map(VeterinarioMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping
    @Operation(summary = "Registrar un veterinario")
    public ResponseEntity<VeterinarioResponse> create(
            @Valid @RequestBody VeterinarioRequest request) {
        Veterinario creado = veterinarioService.create(VeterinarioMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(VeterinarioMapper.toResponse(creado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un veterinario")
    public ResponseEntity<VeterinarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VeterinarioRequest request) {
        Veterinario actualizado = veterinarioService.update(id, VeterinarioMapper.toEntity(request));
        return ResponseEntity.ok(VeterinarioMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un veterinario")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        veterinarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
