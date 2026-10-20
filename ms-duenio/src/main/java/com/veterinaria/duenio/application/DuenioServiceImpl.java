package com.veterinaria.duenio.application;

import com.veterinaria.duenio.domain.exception.DuenioNotFoundException;
import com.veterinaria.duenio.domain.exception.DuplicateCedulaException;
import com.veterinaria.duenio.domain.exception.DuplicateEmailException;
import com.veterinaria.duenio.domain.model.Duenio;
import com.veterinaria.duenio.domain.port.in.DuenioService;
import com.veterinaria.duenio.domain.port.out.DuenioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DuenioServiceImpl implements DuenioService {

    private final DuenioRepositoryPort duenioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Duenio> findAll() {
        return duenioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Duenio findById(Long id) {
        return duenioRepository.findById(id)
                .orElseThrow(() -> new DuenioNotFoundException(id));
    }

    @Override
    @Transactional
    public Duenio create(Duenio duenio) {
        validateUniqueFields(duenio, null);
        return duenioRepository.save(duenio);
    }

    @Override
    @Transactional
    public Duenio update(Long id, Duenio duenio) {
        Duenio existente = findById(id);
        validateUniqueFields(duenio, existente);

        existente.setNombre(duenio.getNombre());
        existente.setApellido(duenio.getApellido());
        existente.setCedula(duenio.getCedula());
        existente.setTelefono(duenio.getTelefono());
        existente.setEmail(duenio.getEmail());

        return duenioRepository.save(existente);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id);
        duenioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Duenio findByEmail(String email) {
        return duenioRepository.findByEmail(email)
                .orElseThrow(() -> new DuenioNotFoundException(
                        "No se encontró un dueño con el email " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Duenio> findByNombre(String nombre) {
        return duenioRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Duenio> findByApellido(String apellido) {
        return duenioRepository.findByApellido(apellido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Duenio> findByNombreAndApellido(String nombre, String apellido) {
        return duenioRepository.findByNombreAndApellido(nombre, apellido);
    }

    private void validateUniqueFields(Duenio nuevo, Duenio existente) {
        boolean cedulaCambio = existente == null || !existente.getCedula().equals(nuevo.getCedula());
        boolean emailCambio = existente == null || !existente.getEmail().equalsIgnoreCase(nuevo.getEmail());

        if (cedulaCambio && duenioRepository.existsByCedula(nuevo.getCedula())) {
            throw new DuplicateCedulaException(nuevo.getCedula());
        }

        if (emailCambio && duenioRepository.existsByEmail(nuevo.getEmail())) {
            throw new DuplicateEmailException(nuevo.getEmail());
        }
    }
}
