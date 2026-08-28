package com.vet_system.Controller;


import com.vet_system.DTO.MascotaDTO;
import com.vet_system.Entity.Mascota;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.MascotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @GetMapping
    public ResponseEntity<List<MascotaDTO>> listarTodas() {
        return ResponseEntity.ok(
                mascotaService.getAllMascotas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    mascotaService.getMascotaById(id)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarMascota(
            @RequestParam Long duenioId,
            @RequestBody MascotaDTO dto) {

        try {
            MascotaDTO nuevaMascota =
                    mascotaService.createMascota(duenioId, dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevaMascota);

        } catch (ResourceNotFoundException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMascota(
            @PathVariable Long id,
            @RequestBody MascotaDTO dto) {

        try {
            return ResponseEntity.ok(
                    mascotaService.updateMascota(id, dto)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(
            @PathVariable Long id) {

        try {
            mascotaService.deleteMascota(id);

            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}