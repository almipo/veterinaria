package com.veterinaria.duenio.application;

import com.veterinaria.duenio.domain.exception.DuenioNotFoundException;
import com.veterinaria.duenio.domain.exception.DuplicateCedulaException;
import com.veterinaria.duenio.domain.exception.DuplicateEmailException;
import com.veterinaria.duenio.domain.model.Duenio;
import com.veterinaria.duenio.domain.port.out.DuenioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuenioServiceImplTest {

    @Mock
    private DuenioRepositoryPort duenioRepository;

    @InjectMocks
    private DuenioServiceImpl duenioService;

    @Test
    void findAll_cuandoListaVacia_retornaListaVacia() {
        when(duenioRepository.findAll()).thenReturn(List.of());

        List<Duenio> resultado = duenioService.findAll();

        assertThat(resultado).isEmpty();
        verify(duenioRepository).findAll();
    }

    @Test
    void findById_cuandoExiste_retornaDuenio() {
        Duenio duenio = crearDuenio(1L, "31541741", "carlos@gmail.com");
        when(duenioRepository.findById(1L)).thenReturn(Optional.of(duenio));

        Duenio resultado = duenioService.findById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Carlos");
    }

    @Test
    void findById_cuandoNoExiste_lanzaDuenioNotFoundException() {
        when(duenioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DuenioNotFoundException.class, () -> duenioService.findById(99L));
    }

    @Test
    void create_cuandoCedulaYEmailNoExisten_guardaDuenio() {
        Duenio nuevo = crearDuenio(null, "31541741", "carlos@gmail.com");
        Duenio guardado = crearDuenio(1L, "31541741", "carlos@gmail.com");

        when(duenioRepository.existsByCedula("31541741")).thenReturn(false);
        when(duenioRepository.existsByEmail("carlos@gmail.com")).thenReturn(false);
        when(duenioRepository.save(nuevo)).thenReturn(guardado);

        Duenio resultado = duenioService.create(nuevo);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(duenioRepository).save(nuevo);
    }

    @Test
    void create_cuandoCedulaDuplicada_lanzaDuplicateCedulaException() {
        Duenio nuevo = crearDuenio(null, "31541741", "carlos@gmail.com");
        when(duenioRepository.existsByCedula("31541741")).thenReturn(true);

        assertThrows(DuplicateCedulaException.class, () -> duenioService.create(nuevo));

        verify(duenioRepository, never()).save(any(Duenio.class));
    }

    @Test
    void create_cuandoEmailDuplicado_lanzaDuplicateEmailException() {
        Duenio nuevo = crearDuenio(null, "31541741", "carlos@gmail.com");
        when(duenioRepository.existsByCedula("31541741")).thenReturn(false);
        when(duenioRepository.existsByEmail("carlos@gmail.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> duenioService.create(nuevo));

        verify(duenioRepository, never()).save(any(Duenio.class));
    }

    @Test
    void update_cuandoExiste_actualizaDatos() {
        Duenio existente = crearDuenio(1L, "31541741", "carlos@gmail.com");
        Duenio cambios = new Duenio(null, "Juan", "Perez", "31541741", 111222333, "juan@gmail.com");

        when(duenioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(duenioRepository.existsByEmail("juan@gmail.com")).thenReturn(false);
        when(duenioRepository.save(existente)).thenReturn(existente);

        Duenio resultado = duenioService.update(1L, cambios);

        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Perez");
        assertThat(resultado.getEmail()).isEqualTo("juan@gmail.com");
        verify(duenioRepository).save(existente);
    }

    @Test
    void deleteById_cuandoExiste_eliminaPorId() {
        Duenio existente = crearDuenio(1L, "31541741", "carlos@gmail.com");
        when(duenioRepository.findById(1L)).thenReturn(Optional.of(existente));

        duenioService.deleteById(1L);

        verify(duenioRepository).deleteById(1L);
    }

    private Duenio crearDuenio(Long id, String cedula, String email) {
        return new Duenio(id, "Carlos", "Sanchez", cedula, 123456789, email);
    }
}
