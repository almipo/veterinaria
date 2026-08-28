package com.vet_system.Controller;
import com.vet_system.DTO.VeterinarioDTO;
import com.vet_system.Entity.Duenio;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.DuenioService;
import com.vet_system.Service.VeterinarioService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/veterinario")
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    public ResponseEntity<List<VeterinarioDTO>> getAllVeterinarios() {
        return ResponseEntity.ok(
                veterinarioService.getAllVeterinarios()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVeterinarioById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    veterinarioService.getVeterinarioById(id)
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createVeterinario(
            @RequestBody VeterinarioDTO dto) {

        try {
            VeterinarioDTO nuevo =
                    veterinarioService.createVeterinario(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevo);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVeterinario(
            @PathVariable Long id,
            @RequestBody VeterinarioDTO dto) {

        try {
            return ResponseEntity.ok(
                    veterinarioService.updateVeterinario(id, dto)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinario(
            @PathVariable Long id) {

        try {
            veterinarioService.deleteVeterinario(id);
            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}