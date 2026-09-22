package com.vet_system.Service;

import com.vet_system.DTO.MascotaDTO;
import com.vet_system.Entity.Duenio;
import com.vet_system.Entity.Mascota;
import com.vet_system.Exception.CupoMascotasException;
import com.vet_system.Mapper.MascotaMapper;
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

    private final MascotaRepository mascotaRepository;
    private final DuenioRepository duenioRepository;
    private final MascotaMapper mascotaMapper;

    public List<MascotaDTO> getAllMascotas() {
        return mascotaRepository.findAll()
                .stream()
                .map(mascotaMapper::toDTO)
                .toList();
    }

    public MascotaDTO getMascotaById(Long id) {

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mascota", id));

        return mascotaMapper.toDTO(mascota);
    }

    @Transactional
    public List<MascotaDTO> getMascotasByDuenio(Long duenioId) {

        if (!duenioRepository.existsById(duenioId)) {
            throw new ResourceNotFoundException("Duenio", duenioId);
        }

        return mascotaRepository.findByDuenioId(duenioId)
                .stream()
                .map(mascotaMapper::toDTO)
                .toList();
    }
    @Transactional
    public MascotaDTO createMascota(
            Long duenioId,
            MascotaDTO dto) {

        Duenio duenio = duenioRepository.findById(duenioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Duenio", duenioId));

        long cantidadMascotas =
                mascotaRepository.countByDuenioId(duenioId);

        if (cantidadMascotas >= 5) {
            throw new CupoMascotasException(
                    "El dueño ya alcanzó el límite de 5 mascotas activas"
            );
        }

        Mascota mascota = new Mascota();

        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setFechaNacimiento(dto.getFechaNacimiento());
        mascota.setDuenio(duenio);

        return mascotaMapper.toDTO(
                mascotaRepository.save(mascota)
        );
    }

    @Transactional
    public MascotaDTO updateMascota(
            Long id,
            MascotaDTO dto) {

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mascota", id));

        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setFechaNacimiento(dto.getFechaNacimiento());

        return mascotaMapper.toDTO(
                mascotaRepository.save(mascota)
        );
    }

    public void deleteMascota(Long id) {

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mascota", id));

        mascotaRepository.delete(mascota);
    }
}