package com.vet_system.Service;

import com.vet_system.Entity.Duenio;
import com.vet_system.Entity.Mascota;
import com.vet_system.Repository.DuenioRepository;
import com.vet_system.Repository.MascotaRepository;
import com.vet_system.Exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))


public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private DuenioRepository duenioRepository;


    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }


    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mascota", id));
    }





    @Transactional
    public Mascota registrarMascota(Long duenioId, Mascota mascota) {

        Duenio duenio = duenioRepository.findById(duenioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Duenio", duenioId));

        if (mascotaRepository.existsByNombreAndDuenoId(
                mascota.getNombre(), duenioId)) {
            throw new RuntimeException("La mascota ya existe para este dueño");
        }

        mascota.setDuenio(duenio);

        return mascotaRepository.save(mascota);
    }


    @Transactional
    public Mascota actualizarMascota(Long id, Mascota datos) {

        Mascota mascota = buscarPorId(id);

        mascota.setNombre(datos.getNombre());
        mascota.setEspecie(datos.getEspecie());
        mascota.setRaza(datos.getRaza());
        mascota.setFechaNacimiento(datos.getFechaNacimiento());

        return mascotaRepository.save(mascota);
    }

    @Transactional
    public List<Mascota> buscarPorDuenio(Long duenioId) {

        if (!duenioRepository.existsById(duenioId)) {
            throw new ResourceNotFoundException("Duenio", duenioId);
        }

        return mascotaRepository.findByDuenoId(duenioId);
    }

    public void eliminarMascota(Long id) {

        Mascota mascota = buscarPorId(id);

        mascotaRepository.delete(mascota);
    }
}