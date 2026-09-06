package com.vet_system.Service;
import com.vet_system.DTO.DuenioDTO;
import com.vet_system.Entity.Duenio;
import com.vet_system.Exception.DuplicateResourceException;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Mapper.DuenioMapper;
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

    private final DuenioRepository duenioRepository;
    private final DuenioMapper duenioMapper;

    public DuenioDTO registrarDuenio(DuenioDTO dto) {

        if (duenioRepository.existsByCedula(dto.getCedula())) {
            throw new DuplicateResourceException(
                    "Ya existe un dueño con el DNI " + dto.getCedula()
            );
        }

        Duenio duenio = duenioMapper.toEntity(dto);
        Duenio guardado = duenioRepository.save(duenio);

        return duenioMapper.toDTO(guardado);
    }

    public List<DuenioDTO> listarTodos() {
        return duenioRepository.findAll()
                .stream()
                .map(duenioMapper::toDTO)
                .toList();
    }

    public DuenioDTO buscarPorId(Long id) {

        Duenio duenio = duenioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Duenio", id));

        return duenioMapper.toDTO(duenio);
    }

    public DuenioDTO updateDuenio(Long id, DuenioDTO dto) {

        Duenio duenio = duenioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Duenio", id));

        duenio.setNombre(dto.getNombre());
        duenio.setApellido(dto.getApellido());
        duenio.setTelefono(dto.getTelefono());
        duenio.setEmail(dto.getEmail());

        return duenioMapper.toDTO(
                duenioRepository.save(duenio)
        );
    }

    public void deleteDuenio(Long id) {

        Duenio duenio = duenioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Duenio", id));

        duenioRepository.delete(duenio);
    }

    public Optional<DuenioDTO> buscarPorEmail(String email) {
        return duenioRepository.findByEmail(email)
                .map(duenioMapper::toDTO);
    }

    public List<DuenioDTO> buscarPorNombre(String nombre) {
        return duenioRepository.findByNombre(nombre)
                .stream()
                .map(duenioMapper::toDTO)
                .toList();
    }

    public List<DuenioDTO> buscarPorApellido(String apellido) {
        return duenioRepository.findByApellido(apellido)
                .stream()
                .map(duenioMapper::toDTO)
                .toList();
    }

    public List<DuenioDTO> buscarPorNombreYApellido(
            String nombre,
            String apellido) {

        return duenioRepository
                .findByNombreAndApellido(nombre, apellido)
                .stream()
                .map(duenioMapper::toDTO)
                .toList();
    }
}