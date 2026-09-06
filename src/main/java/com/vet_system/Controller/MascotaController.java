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
    public ResponseEntity<MascotaDTO> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(
                mascotaService.getMascotaById(id)
        );
    }

    @PostMapping
    public ResponseEntity<MascotaDTO> registrarMascota(
            @RequestParam Long duenioId,
            @Valid @RequestBody MascotaDTO dto) {

        MascotaDTO nuevaMascota =
                mascotaService.createMascota(duenioId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaMascota);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaDTO> actualizarMascota(
            @PathVariable Long id,
            @Valid @RequestBody MascotaDTO dto) {

        return ResponseEntity.ok(
                mascotaService.updateMascota(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(
            @PathVariable Long id) {

        mascotaService.deleteMascota(id);

        return ResponseEntity.noContent().build();
    }}