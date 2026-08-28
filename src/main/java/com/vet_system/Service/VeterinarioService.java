package com.vet_system.Service;

import com.vet_system.DTO.VeterinarioDTO;
import com.vet_system.Entity.Veterinario;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Mapper.VeterinarioMapper;
import com.vet_system.Repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final VeterinarioMapper veterinarioMapper;

    public List<VeterinarioDTO> getAllVeterinarios() {
        return veterinarioRepository.findAll()
                .stream()
                .map(veterinarioMapper::toDTO)
                .toList();
    }

    public VeterinarioDTO getVeterinarioById(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Veterinario", id));

        return veterinarioMapper.toDTO(veterinario);
    }

    public VeterinarioDTO createVeterinario(VeterinarioDTO dto) {

        if (veterinarioRepository.existsByMatricula(dto.getMatricula())) {
            throw new RuntimeException(
                    "Matrícula ya registrada: " + dto.getMatricula()
            );
        }

        Veterinario veterinario = veterinarioMapper.toEntity(dto);

        Veterinario veterinarioGuardado =
                veterinarioRepository.save(veterinario);

        return veterinarioMapper.toDTO(veterinarioGuardado);
    }

    public VeterinarioDTO updateVeterinario(Long id, VeterinarioDTO dto) {

        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Veterinario", id));

        veterinario.setNombre(dto.getNombre());
        veterinario.setApellido(dto.getApellido());
        veterinario.setEspecialidad(dto.getEspecialidad());

        return veterinarioMapper.toDTO(
                veterinarioRepository.save(veterinario)
        );
    }

    public void deleteVeterinario(Long id) {

        if (!veterinarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veterinario", id);
        }

        veterinarioRepository.deleteById(id);
    }
}