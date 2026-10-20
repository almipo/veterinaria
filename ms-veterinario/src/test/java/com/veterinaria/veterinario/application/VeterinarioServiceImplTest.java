package com.veterinaria.veterinario.application;

import com.veterinaria.veterinario.domain.exception.DuplicateMatriculaException;
import com.veterinaria.veterinario.domain.exception.VeterinarioNotFoundException;
import com.veterinaria.veterinario.domain.model.Especialidad;
import com.veterinaria.veterinario.domain.model.Veterinario;
import com.veterinaria.veterinario.domain.port.out.VeterinarioRepositoryPort;
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
class VeterinarioServiceImplTest {

    @Mock
    private VeterinarioRepositoryPort veterinarioRepository;

    @InjectMocks
    private VeterinarioServiceImpl veterinarioService;

    @Test
    void findAll_retornaVeterinarios() {
        when(veterinarioRepository.findAll())
                .thenReturn(List.of(crearVeterinario(1L, "MN-1234")));

        List<Veterinario> resultado = veterinarioService.findAll();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void findById_cuandoExiste_retornaVeterinario() {
        Veterinario vet = crearVeterinario(1L, "MN-1234");
        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(vet));

        Veterinario resultado = veterinarioService.findById(1L);

        assertThat(resultado.getMatricula()).isEqualTo("MN-1234");
    }

    @Test
    void findById_cuandoNoExiste_lanzaVeterinarioNotFoundException() {
        when(veterinarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(VeterinarioNotFoundException.class,
                () -> veterinarioService.findById(99L));
    }

    @Test
    void findByEspecialidad_retornaCoincidencias() {
        when(veterinarioRepository.findByEspecialidad(Especialidad.CLINICA_GENERAL))
                .thenReturn(List.of(crearVeterinario(1L, "MN-1234")));

        List<Veterinario> resultado = veterinarioService.findByEspecialidad(Especialidad.CLINICA_GENERAL);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void create_cuandoMatriculaNoExiste_guardaVeterinario() {
        Veterinario nuevo = crearVeterinario(null, "MN-1234");
        Veterinario guardado = crearVeterinario(1L, "MN-1234");

        when(veterinarioRepository.existsByMatricula("MN-1234")).thenReturn(false);
        when(veterinarioRepository.save(nuevo)).thenReturn(guardado);

        Veterinario resultado = veterinarioService.create(nuevo);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(veterinarioRepository).save(nuevo);
    }

    @Test
    void create_cuandoMatriculaDuplicada_lanzaDuplicateMatriculaException() {
        Veterinario nuevo = crearVeterinario(null, "MN-1234");
        when(veterinarioRepository.existsByMatricula("MN-1234")).thenReturn(true);

        assertThrows(DuplicateMatriculaException.class,
                () -> veterinarioService.create(nuevo));

        verify(veterinarioRepository, never()).save(any(Veterinario.class));
    }

    @Test
    void update_cuandoExiste_actualizaDatos() {
        Veterinario existente = crearVeterinario(1L, "MN-1234");
        Veterinario cambios = new Veterinario(null, "Ana", "Perez", "MN-1234",
                Especialidad.DERMATOLOGIA, "ana@vet.com", 111222333);

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(veterinarioRepository.save(existente)).thenReturn(existente);

        Veterinario resultado = veterinarioService.update(1L, cambios);

        assertThat(resultado.getNombre()).isEqualTo("Ana");
        assertThat(resultado.getEspecialidad()).isEqualTo(Especialidad.DERMATOLOGIA);
    }

    @Test
    void deleteById_cuandoExiste_eliminaPorId() {
        when(veterinarioRepository.findById(1L))
                .thenReturn(Optional.of(crearVeterinario(1L, "MN-1234")));

        veterinarioService.deleteById(1L);

        verify(veterinarioRepository).deleteById(1L);
    }

    private Veterinario crearVeterinario(Long id, String matricula) {
        return new Veterinario(id, "Marta", "Lopez", matricula,
                Especialidad.CLINICA_GENERAL, "marta@patitas.com", 123456789);
    }
}
