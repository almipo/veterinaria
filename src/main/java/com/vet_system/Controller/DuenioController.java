package com.vet_system.Controller;
import com.vet_system.Entity.Duenio;
import com.vet_system.Service.DuenioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/duenio")
public class DuenioController {

    @Autowired
    private DuenioService duenioService;

    // POST - crear dueño
    @PostMapping
    public ResponseEntity<Duenio> registrarDuenio(@RequestBody Duenio duenio) {

        Duenio duenioGuardado = duenioService.registrarDuenio(duenio);

        URI location = URI.create("/api/duenio/" + duenioGuardado.getId());

        return ResponseEntity
                .created(location)
                .body(duenioGuardado);
    }

    // GET - buscar dueño por ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Duenio>> buscarPorId(@PathVariable Long id){
        Optional<Duenio> duenioBuscado= duenioService.buscarPorId(id);
        if(duenioBuscado.isPresent()){
            return ResponseEntity.ok(duenioBuscado);
        }else {
            return ResponseEntity.notFound().build();
        }
    }


    // GET - listar todos los dueños
    @GetMapping
    public ResponseEntity<List<Duenio>> getAllDuenos() {

        return ResponseEntity.ok(duenioService.getAllDuenios());
    }
    // PUT - modificar dueño
    @PutMapping("/{id}")
    public ResponseEntity<Duenio> updateDueno(
            @PathVariable Long id,
            @RequestBody Duenio duenio) {

        Duenio duenioActualizado =
                duenioService.updateDueno(id, duenio);

        return ResponseEntity.ok(duenioActualizado);
    }

    // DELETE - eliminar dueño
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDueno(@PathVariable Long id) {

        duenioService.deleteDueno(id);

        return ResponseEntity.noContent().build();
    }

// GET - buscar dueño por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<Duenio>> buscarPorEmail(
            @PathVariable String email) {

        Optional<Duenio> duenioBuscado =
                duenioService.buscarPorEmail(email);

        if (duenioBuscado.isPresent()) {
            return ResponseEntity.ok(duenioBuscado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Duenio>> buscarPorNombre(
            @PathVariable String nombre) {

        return ResponseEntity.ok(
                duenioService.buscarPorNombre(nombre)
        );
    }


    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Duenio>> buscarPorApellido(
            @PathVariable String apellido) {

        return ResponseEntity.ok(
                duenioService.buscarPorApellido(apellido)
        );
    }


    @GetMapping("/nombre/{nombre}/apellido/{apellido}")
    public ResponseEntity<List<Duenio>> buscarPorNombreYApellido(
            @PathVariable String nombre,
            @PathVariable String apellido) {

        return ResponseEntity.ok(
                duenioService.buscarPorNombreYApellido(nombre, apellido)
        );
    }
}
