package com.veterinaria.duenio.adapter.in.web;

import com.veterinaria.duenio.adapter.in.web.dto.DuenioMapper;
import com.veterinaria.duenio.adapter.in.web.dto.DuenioRequest;
import com.veterinaria.duenio.adapter.in.web.dto.DuenioResponse;
import com.veterinaria.duenio.domain.model.Duenio;
import com.veterinaria.duenio.domain.port.in.DuenioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/duenio")
@RequiredArgsConstructor
@Tag(name = "Dueños", description = "Operaciones para gestionar dueños")
public class DuenioController {

    private final DuenioService duenioService;

    @PostMapping
    @Operation(summary = "Registrar un dueño")
    public ResponseEntity<DuenioResponse> create(
            @Valid @RequestBody DuenioRequest request) {
        Duenio creado = duenioService.create(DuenioMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DuenioMapper.toResponse(creado));
    }

    @GetMapping
    @Operation(summary = "Listar todos los dueños")
    public ResponseEntity<List<DuenioResponse>> findAll() {
        List<DuenioResponse> response = duenioService.findAll()
                .stream()
                .map(DuenioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dueño por ID")
    public ResponseEntity<DuenioResponse> findById(
            @Parameter(description = "ID del dueño", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(
                DuenioMapper.toResponse(duenioService.findById(id))
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un dueño")
    public ResponseEntity<DuenioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DuenioRequest request) {
        Duenio actualizado = duenioService.update(id, DuenioMapper.toEntity(request));
        return ResponseEntity.ok(DuenioMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un dueño")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        duenioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar dueño por email")
    public ResponseEntity<DuenioResponse> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(
                DuenioMapper.toResponse(duenioService.findByEmail(email))
        );
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar dueños por nombre")
    public ResponseEntity<List<DuenioResponse>> findByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(
                duenioService.findByNombre(nombre)
                        .stream()
                        .map(DuenioMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/apellido/{apellido}")
    @Operation(summary = "Buscar dueños por apellido")
    public ResponseEntity<List<DuenioResponse>> findByApellido(@PathVariable String apellido) {
        return ResponseEntity.ok(
                duenioService.findByApellido(apellido)
                        .stream()
                        .map(DuenioMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar dueños por nombre y apellido")
    public ResponseEntity<List<DuenioResponse>> findByNombreAndApellido(
            @RequestParam String nombre,
            @RequestParam String apellido) {
        return ResponseEntity.ok(
                duenioService.findByNombreAndApellido(nombre, apellido)
                        .stream()
                        .map(DuenioMapper::toResponse)
                        .toList()
        );
    }
}
