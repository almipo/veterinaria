package com.vet_system.Controller;

import com.vet_system.DTO.DuenioDTO;
import com.vet_system.DTO.MascotaDTO;
import com.vet_system.Service.DuenioService;
import com.vet_system.Service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/duenio")
@RequiredArgsConstructor
public class DuenioController {

    private final DuenioService duenioService;
    private final MascotaService mascotaService;

    @PostMapping
    public ResponseEntity<DuenioDTO> registrarDuenio(
            @Valid @RequestBody DuenioDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(duenioService.registrarDuenio(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuenioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                duenioService.buscarPorId(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<DuenioDTO>> listarTodos() {
        return ResponseEntity.ok(
                duenioService.listarTodos()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DuenioDTO> actualizarDuenio(
            @PathVariable Long id,
            @Valid @RequestBody DuenioDTO dto) {

        return ResponseEntity.ok(
                duenioService.updateDuenio(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDuenio(
            @PathVariable Long id) {

        duenioService.deleteDuenio(id);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<List<MascotaDTO>> getMascotasByDuenio(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                mascotaService.getMascotasByDuenio(id)
        );
    }
}