package com.vet_system.Controller;
import com.vet_system.DTO.DuenioDTO;
import com.vet_system.Entity.Duenio;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.DuenioService;
import com.vet_system.Service.MascotaService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/duenio")

@RequiredArgsConstructor
public class DuenioController {

    private final DuenioService duenioService;
    private final MascotaService mascotaService;
    @PostMapping
    public ResponseEntity<DuenioDTO> registrarDuenio(
            @RequestBody DuenioDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(duenioService.registrarDuenio(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    duenioService.buscarPorId(id)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DuenioDTO>> listarTodos() {

        return ResponseEntity.ok(
                duenioService.listarTodos()
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDuenio(
            @PathVariable Long id,
            @RequestBody DuenioDTO dto) {

        try {
            return ResponseEntity.ok(
                    duenioService.updateDuenio(id, dto)
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDuenio(
            @PathVariable Long id) {

        try {
            duenioService.deleteDuenio(id);
            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {

        return duenioService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<DuenioDTO>> buscarPorNombre(
            @PathVariable String nombre) {

        return ResponseEntity.ok(
                duenioService.buscarPorNombre(nombre)
        );
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<DuenioDTO>> buscarPorApellido(
            @PathVariable String apellido) {

        return ResponseEntity.ok(
                duenioService.buscarPorApellido(apellido)
        );
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<DuenioDTO>> buscarPorNombreYApellido(
            @RequestParam String nombre,
            @RequestParam String apellido) {

        return ResponseEntity.ok(
                duenioService.buscarPorNombreYApellido(nombre, apellido)
        );
    }

    @GetMapping("/{id}/mascotas")
    public ResponseEntity<?> getMascotasByDuenio(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    mascotaService.getMascotasByDuenio(id)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}