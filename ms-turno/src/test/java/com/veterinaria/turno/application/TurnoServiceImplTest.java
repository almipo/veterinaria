package com.veterinaria.turno.application;

import com.veterinaria.turno.domain.exception.MascotaNotFoundException;
import com.veterinaria.turno.domain.exception.TurnoNotFoundException;
import com.veterinaria.turno.domain.exception.TurnoSuperpuestoException;
import com.veterinaria.turno.domain.exception.VeterinarioNotFoundException;
import com.veterinaria.turno.domain.model.EstadoTurno;
import com.veterinaria.turno.domain.model.Turno;
import com.veterinaria.turno.domain.port.out.MascotaServicePort;
import com.veterinaria.turno.domain.port.out.TurnoRepositoryPort;
import com.veterinaria.turno.domain.port.out.VeterinarioServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnoServiceImplTest {

    @Mock
    private TurnoRepositoryPort turnoRepository;

    @Mock
    private MascotaServicePort mascotaService;

    @Mock
    private VeterinarioServicePort veterinarioService;

    @InjectMocks
    private TurnoServiceImpl turnoService;

    @Test
    void create_cuandoDatosValidos_guardaTurnoPendiente() {
        Turno turno = crearTurno(null, 1L, 2L);
        Turno guardado = crearTurno(5L, 1L, 2L);

        when(mascotaService.existsById(1L)).thenReturn(true);
        when(veterinarioService.existsById(2L)).thenReturn(true);
        when(turnoRepository.existsByVeterinarioIdAndFechaHora(2L, turno.getFechaHora()))
                .thenReturn(false);
        when(turnoRepository.save(turno)).thenReturn(guardado);

        Turno resultado = turnoService.create(turno);

        assertThat(resultado.getId()).isEqualTo(5L);
        assertThat(turno.getEstado()).isEqualTo(EstadoTurno.PENDIENTE);
        verify(turnoRepository).save(turno);
    }

    @Test
    void create_cuandoMascotaNoExiste_lanzaMascotaNotFoundException() {
        Turno turno = crearTurno(null, 999L, 2L);
        when(mascotaService.existsById(999L)).thenReturn(false);

        assertThrows(MascotaNotFoundException.class, () -> turnoService.create(turno));

        verifyNoInteractions(veterinarioService);
        verify(turnoRepository, never()).save(any(Turno.class));
    }

    @Test
    void create_cuandoVeterinarioNoExiste_lanzaVeterinarioNotFoundException() {
        Turno turno = crearTurno(null, 1L, 999L);
        when(mascotaService.existsById(1L)).thenReturn(true);
        when(veterinarioService.existsById(999L)).thenReturn(false);

        assertThrows(VeterinarioNotFoundException.class, () -> turnoService.create(turno));

        verify(turnoRepository, never()).save(any(Turno.class));
    }

    @Test
    void create_cuandoHaySuperposicion_lanzaTurnoSuperpuestoException() {
        Turno turno = crearTurno(null, 1L, 2L);
        when(mascotaService.existsById(1L)).thenReturn(true);
        when(veterinarioService.existsById(2L)).thenReturn(true);
        when(turnoRepository.existsByVeterinarioIdAndFechaHora(2L, turno.getFechaHora()))
                .thenReturn(true);

        assertThrows(TurnoSuperpuestoException.class, () -> turnoService.create(turno));

        verify(turnoRepository, never()).save(any(Turno.class));
    }

    @Test
    void findById_cuandoExiste_retornaTurno() {
        Turno turno = crearTurno(5L, 1L, 2L);
        when(turnoRepository.findById(5L)).thenReturn(Optional.of(turno));

        Turno resultado = turnoService.findById(5L);

        assertThat(resultado.getId()).isEqualTo(5L);
    }

    @Test
    void findById_cuandoNoExiste_lanzaTurnoNotFoundException() {
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TurnoNotFoundException.class, () -> turnoService.findById(99L));
    }

    @Test
    void cancel_cambiaEstadoACancelado() {
        Turno turno = crearTurno(5L, 1L, 2L);
        when(turnoRepository.findById(5L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(turno)).thenReturn(turno);

        Turno resultado = turnoService.cancel(5L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoTurno.CANCELADO);
        verify(turnoRepository).save(turno);
    }

    private Turno crearTurno(Long id, Long mascotaId, Long veterinarioId) {
        return new Turno(
                id,
                LocalDateTime.of(2026, 11, 15, 10, 0),
                mascotaId,
                veterinarioId,
                "Control anual",
                EstadoTurno.PENDIENTE,
                null
        );
    }
}
