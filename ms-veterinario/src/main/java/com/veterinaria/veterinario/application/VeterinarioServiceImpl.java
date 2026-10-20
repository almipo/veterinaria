package com.veterinaria.veterinario.application;

import com.veterinaria.veterinario.domain.exception.DuplicateMatriculaException;
import com.veterinaria.veterinario.domain.exception.VeterinarioNotFoundException;
import com.veterinaria.veterinario.domain.model.Especialidad;
import com.veterinaria.veterinario.domain.model.Veterinario;
import com.veterinaria.veterinario.domain.port.in.VeterinarioService;
import com.veterinaria.veterinario.domain.port.out.VeterinarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinarioServiceImpl implements VeterinarioService {

    private final VeterinarioRepositoryPort veterinarioRepository;

    @Override
    public List<Veterinario> findAll() {
        return veterinarioRepository.findAll();
    }

    @Override
    public Veterinario findById(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> new VeterinarioNotFoundException(id));
    }

    @Override
    public List<Veterinario> findByEspecialidad(Especialidad especialidad) {
        return veterinarioRepository.findByEspecialidad(especialidad);
    }

    @Override
    @Transactional
    public Veterinario create(Veterinario veterinario) {
        if (veterinarioRepository.existsByMatricula(veterinario.getMatricula())) {
            throw new DuplicateMatriculaException(veterinario.getMatricula());
        }
        veterinario.setId(null);
        return veterinarioRepository.save(veterinario);
    }

    @Override
    @Transactional
    public Veterinario update(Long id, Veterinario datos) {
        Veterinario actual = findById(id);

        if (!actual.getMatricula().equals(datos.getMatricula())
                && veterinarioRepository.existsByMatricula(datos.getMatricula())) {
            throw new DuplicateMatriculaException(datos.getMatricula());
        }

        actual.setNombre(datos.getNombre());
        actual.setApellido(datos.getApellido());
        actual.setMatricula(datos.getMatricula());
        actual.setEspecialidad(datos.getEspecialidad());
        actual.setEmail(datos.getEmail());
        actual.setTelefono(datos.getTelefono());

        return veterinarioRepository.save(actual);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id);
        veterinarioRepository.deleteById(id);
    }
}
