package com.vet_system.Service;

import com.vet_system.DTO.TurnoRequestDTO;
import com.vet_system.DTO.TurnoResponseDTO;
import com.vet_system.Entity.Mascota;
import com.vet_system.Entity.Turno;
import com.vet_system.Entity.Veterinario;
import com.vet_system.Exception.TurnoSuperpuestoException;
import com.vet_system.Mapper.TurnoMapper;
import com.vet_system.Repository.MascotaRepository;
import com.vet_system.Repository.TurnoRepository;
import com.vet_system.Repository.VeterinarioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnoServiceTest {

    @InjectMocks
    private TurnoService turnoService;

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @Mock
    private TurnoMapper turnoMapper;


    // TEST 1
    @Test
    void createTurno_cuandoNoHaySuperposicion_guardaTurno() {

        // DADO
        TurnoRequestDTO request = new TurnoRequestDTO();
        request.setFecha(LocalDate.of(2026, 10, 10));
        request.setHora(LocalTime.of(10, 30));
        request.setMotivo("Control");
        request.setMascotaId(1L);
        request.setVeterinarioId(2L);

        Mascota mascota = new Mascota();
        mascota.setId(1L);

        Veterinario veterinario = new Veterinario();
        veterinario.setId(2L);

        Turno turnoGuardado = new Turno();
        turnoGuardado.setId(5L);
        turnoGuardado.setFecha(request.getFecha());
        turnoGuardado.setHora(request.getHora());

        TurnoResponseDTO responseDTO = new TurnoResponseDTO();
        responseDTO.setId(5L);

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(veterinarioRepository.findById(2L))
                .thenReturn(Optional.of(veterinario));

        when(turnoRepository.existsByVeterinarioIdAndFechaAndHora(
                2L,
                request.getFecha(),
                request.getHora()
        )).thenReturn(false);

        when(turnoRepository.save(any(Turno.class)))
                .thenReturn(turnoGuardado);

        when(turnoMapper.toDTO(turnoGuardado))
                .thenReturn(responseDTO);

        // CUANDO
        TurnoResponseDTO resultado =
                turnoService.createTurno(request);

        // ENTONCES
        assertThat(resultado.getId()).isEqualTo(5L);

        verify(turnoRepository, times(1))
                .save(any(Turno.class));
    }


    // TEST 2
    @Test
    void createTurno_cuandoHaySuperposicion_lanzaTurnoSuperpuestoException() {

        // DADO
        TurnoRequestDTO request = new TurnoRequestDTO();
        request.setFecha(LocalDate.of(2026, 10, 10));
        request.setHora(LocalTime.of(10, 30));
        request.setMotivo("Control");
        request.setMascotaId(1L);
        request.setVeterinarioId(2L);

        Mascota mascota = new Mascota();
        mascota.setId(1L);

        Veterinario veterinario = new Veterinario();
        veterinario.setId(2L);

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(veterinarioRepository.findById(2L))
                .thenReturn(Optional.of(veterinario));

        when(turnoRepository.existsByVeterinarioIdAndFechaAndHora(
                2L,
                request.getFecha(),
                request.getHora()
        )).thenReturn(true);

        // CUANDO - ENTONCES
        assertThrows(
                TurnoSuperpuestoException.class,
                () -> turnoService.createTurno(request)
        );

        verify(turnoRepository, never())
                .save(any(Turno.class));
    }
}