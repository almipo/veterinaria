package com.veterinaria.mascota.application;

import com.veterinaria.mascota.domain.exception.MascotaNotFoundException;
import com.veterinaria.mascota.domain.model.Mascota;
import com.veterinaria.mascota.domain.port.out.MascotaRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceImplTest {

    @Mock
    private MascotaRepositoryPort mascotaRepository;

    @InjectMocks
    private MascotaServiceImpl mascotaService;

    @Test
    void findAll_retornaMascotas() {
        Mascota luna = crearMascota(1L, "Luna", 10L);
        when(mascotaRepository.findAll()).thenReturn(List.of(luna));

        List<Mascota> resultado = mascotaService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getNombre()).isEqualTo("Luna");
    }

    @Test
    void findById_cuandoExiste_retornaMascota() {
        Mascota luna = crearMascota(1L, "Luna", 10L);
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(luna));

        Mascota resultado = mascotaService.findById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void findById_cuandoNoExiste_lanzaMascotaNotFoundException() {
        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MascotaNotFoundException.class, () -> mascotaService.findById(99L));
    }

    @Test
    void findByDuenioId_retornaMascotasDelDuenio() {
        when(mascotaRepository.findByDuenioId(10L))
                .thenReturn(List.of(crearMascota(1L, "Luna", 10L)));

        List<Mascota> resultado = mascotaService.findByDuenioId(10L);

        assertThat(resultado).hasSize(1);
        verify(mascotaRepository).findByDuenioId(10L);
    }

    @Test
    void create_guardaMascota() {
        Mascota nueva = crearMascota(null, "Luna", 10L);
        Mascota guardada = crearMascota(1L, "Luna", 10L);
        when(mascotaRepository.save(nueva)).thenReturn(guardada);

        Mascota resultado = mascotaService.create(nueva);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(mascotaRepository).save(nueva);
    }

    @Test
    void update_cuandoExiste_actualizaDatos() {
        Mascota existente = crearMascota(1L, "Luna", 10L);
        Mascota cambios = new Mascota(null, "Mora", "Perro", "Caniche",
                LocalDate.of(2022, 2, 2), 20L);

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(mascotaRepository.save(existente)).thenReturn(existente);

        Mascota resultado = mascotaService.update(1L, cambios);

        assertThat(resultado.getNombre()).isEqualTo("Mora");
        assertThat(resultado.getDuenioId()).isEqualTo(20L);
    }

    @Test
    void deleteById_cuandoExiste_eliminaPorId() {
        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(crearMascota(1L, "Luna", 10L)));

        mascotaService.deleteById(1L);

        verify(mascotaRepository).deleteById(1L);
    }

    private Mascota crearMascota(Long id, String nombre, Long duenioId) {
        return new Mascota(id, nombre, "Gato", "Siames",
                LocalDate.of(2023, 5, 10), duenioId);
    }
}
