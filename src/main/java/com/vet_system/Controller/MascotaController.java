package com.vet_system.Controller;


import com.vet_system.Entity.Mascota;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mascota")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;


    @GetMapping
    public ResponseEntity<List<Mascota>> listarTodas() {
        return ResponseEntity.ok(mascotaService.listarTodas());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Mascota> buscarPorId(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(mascotaService.buscarPorId(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<?> registrarMascota(
            @RequestParam Long duenioId,
            @RequestBody Mascota mascota) {

        try {

            Mascota nuevaMascota =
                    mascotaService.registrarMascota(duenioId, mascota);

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
            @RequestBody Mascota mascota) {

        try {
            return ResponseEntity.ok(
                    mascotaService.actualizarMascota(id, mascota)
            );

        } catch (ResourceNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {

        try {

            mascotaService.eliminarMascota(id);

            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }
}


