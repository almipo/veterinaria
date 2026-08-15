package com.vet_system.Service;
import com.vet_system.Entity.Duenio;
import com.vet_system.Repository.DuenioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))


public class DuenioService {
    @Autowired
    private final DuenioRepository duenioRepository;


    public Duenio registrarDuenio(Duenio duenio) {
        if (duenioRepository.existsByCedula(duenio.getCedula())) {
            throw new RuntimeException("Ya existe un dueño con ese DNI");
        }
        return duenioRepository.save(duenio);
    }


    public Optional<Duenio> buscarPorId(Long id) {
        return duenioRepository.findById(id);
    }


    public List<Duenio> getAllDuenios() {
        return duenioRepository.findAll();
    }


    public Duenio updateDueno(Long id, Duenio duenioActualizado) {

        Duenio duenio = duenioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Dueño no encontrado"));

        duenio.setNombre(duenioActualizado.getNombre());
        duenio.setApellido(duenioActualizado.getApellido());
        duenio.setTelefono(duenioActualizado.getTelefono());
        duenio.setEmail(duenioActualizado.getEmail());

        return duenioRepository.save(duenio);
    }


    public void deleteDueno(Long id) {

        Duenio duenio = duenioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Dueño no encontrado"));

        duenioRepository.delete(duenio);
    }

    public Optional<Duenio> buscarPorEmail(String email) {
        return duenioRepository.findByEmail(email);
    }

    public List<Duenio> buscarPorNombre(String nombre) {
        return duenioRepository.findByNombre(nombre);
    }

    public List<Duenio> buscarPorApellido(String apellido) {
        return duenioRepository.findByApellido(apellido);
    }

    public List<Duenio> buscarPorNombreYApellido(String nombre, String apellido) {
        return duenioRepository.findByNombreAndApellido(nombre, apellido);
    }

}