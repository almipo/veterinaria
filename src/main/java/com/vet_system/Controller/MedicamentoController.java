package com.vet_system.Controller;

import com.vet_system.DTO.MedicamentoRequestDTO;
import com.vet_system.DTO.MedicamentoResponseDTO;
import com.vet_system.Service.MedicamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @GetMapping
    public ResponseEntity<List<MedicamentoResponseDTO>> getAllMedicamentos() {

        return ResponseEntity.ok(
                medicamentoService.getAllMedicamentos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoResponseDTO> getMedicamentoById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                medicamentoService.getMedicamentoById(id)
        );
    }

    @PostMapping
    public ResponseEntity<MedicamentoResponseDTO> createMedicamento(
            @Valid @RequestBody MedicamentoRequestDTO dto) {

        MedicamentoResponseDTO medicamentoCreado =
                medicamentoService.createMedicamento(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(medicamentoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoResponseDTO> updateMedicamento(
            @PathVariable Long id,
            @Valid @RequestBody MedicamentoRequestDTO dto) {

        return ResponseEntity.ok(
                medicamentoService.updateMedicamento(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicamento(
            @PathVariable Long id) {

        medicamentoService.deleteMedicamento(id);

        return ResponseEntity.noContent().build();
    }
}