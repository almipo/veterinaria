package com.vet_system.Service;

import com.vet_system.DTO.DuenioDTO;
import com.vet_system.Entity.Duenio;
import com.vet_system.Exception.DuplicateResourceException;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Mapper.DuenioMapper;
import com.vet_system.Repository.DuenioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuenioServiceTest {

    @InjectMocks
    private DuenioService duenioService;

    @Mock
    private DuenioMapper duenioMapper;

    @Mock
    private DuenioRepository duenioRepository;


    // TEST 1
    @Test
    void listarTodos_cuandoListaVacia_retornaListaVacia() {

        // DADO
        when(duenioRepository.findAll())
                .thenReturn(List.of());

        // CUANDO
        List<DuenioDTO> resultado = duenioService.listarTodos();

        // ENTONCES
        assertThat(resultado).isEmpty();

        verify(duenioRepository).findAll();
    }


    // TEST 2
    @Test
    void listarTodos_cuandoHayDuenios_retornaListaDTO() {

        // DADO
        Duenio duenio = new Duenio();
        duenio.setId(1L);
        duenio.setNombre("Carlos");

        DuenioDTO dto = new DuenioDTO();
        dto.setId(1L);
        dto.setNombre("Carlos");

        when(duenioRepository.findAll())
                .thenReturn(List.of(duenio));

        when(duenioMapper.toDTO(duenio))
                .thenReturn(dto);

        // CUANDO
        List<DuenioDTO> resultado = duenioService.listarTodos();

        // ENTONCES
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre())
                .isEqualTo("Carlos");

        verify(duenioRepository).findAll();
        verify(duenioMapper).toDTO(duenio);
    }


    // TEST 3
    @Test
    void buscarPorId_cuandoExiste_retornaDTO() {

        // DADO
        Duenio duenio = new Duenio();
        duenio.setId(1L);
        duenio.setNombre("Carlos");
        duenio.setApellido("Sanchez");
        duenio.setCedula("31541741");
        duenio.setEmail("carlos@gmail.com");

        DuenioDTO dto = new DuenioDTO();
        dto.setId(1L);
        dto.setNombre("Carlos");

        when(duenioRepository.findById(1L))
                .thenReturn(Optional.of(duenio));

        when(duenioMapper.toDTO(duenio))
                .thenReturn(dto);

        // CUANDO
        DuenioDTO resultado = duenioService.buscarPorId(1L);

        // ENTONCES
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Carlos");

        verify(duenioRepository).findById(1L);
        verify(duenioMapper).toDTO(duenio);
    }


    // TEST 4
    @Test
    void buscarPorId_cuandoNoExiste_lanzaResourceNotFoundException() {

        // DADO
        when(duenioRepository.findById(99L))
                .thenReturn(Optional.empty());

        // CUANDO - ENTONCES
        assertThrows(
                ResourceNotFoundException.class,
                () -> duenioService.buscarPorId(99L)
        );

        verify(duenioRepository).findById(99L);
        verifyNoInteractions(duenioMapper);
    }


    // TEST 5
    @Test
    void registrarDuenio_cuandoCedulaNoExiste_guardaDuenio() {

        // DADO
        DuenioDTO dto = new DuenioDTO();
        dto.setNombre("Carlos");
        dto.setApellido("Sanchez");
        dto.setCedula("31541741");
        dto.setEmail("carlos@gmail.com");

        Duenio duenio = new Duenio();
        duenio.setNombre("Carlos");
        duenio.setCedula("31541741");

        Duenio duenioGuardado = new Duenio();
        duenioGuardado.setId(1L);
        duenioGuardado.setNombre("Carlos");
        duenioGuardado.setCedula("31541741");

        DuenioDTO dtoGuardado = new DuenioDTO();
        dtoGuardado.setId(1L);
        dtoGuardado.setNombre("Carlos");
        dtoGuardado.setCedula("31541741");

        when(duenioRepository.existsByCedula("31541741"))
                .thenReturn(false);

        when(duenioMapper.toEntity(dto))
                .thenReturn(duenio);

        when(duenioRepository.save(duenio))
                .thenReturn(duenioGuardado);

        when(duenioMapper.toDTO(duenioGuardado))
                .thenReturn(dtoGuardado);

        // CUANDO
        DuenioDTO resultado = duenioService.registrarDuenio(dto);

        // ENTONCES
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Carlos");

        verify(duenioRepository).existsByCedula("31541741");
        verify(duenioRepository).save(duenio);
    }


    // TEST 6
    @Test
    void registrarDuenio_cuandoCedulaDuplicada_lanzaDuplicateResourceException() {

        // DADO
        DuenioDTO dto = new DuenioDTO();
        dto.setCedula("31541741");

        when(duenioRepository.existsByCedula("31541741"))
                .thenReturn(true);

        // CUANDO - ENTONCES
        assertThrows(
                DuplicateResourceException.class,
                () -> duenioService.registrarDuenio(dto)
        );

        verify(duenioRepository).existsByCedula("31541741");

        verify(duenioRepository, never())
                .save(any(Duenio.class));

        verifyNoInteractions(duenioMapper);
    }


    @Test
    void updateDuenio_cuandoExiste_actualizaDTO() {

        // DADO
        Duenio duenioExistente = new Duenio();
        duenioExistente.setId(1L);
        duenioExistente.setNombre("Carlos");
        duenioExistente.setApellido("Sanchez");

        DuenioDTO dtoActualizado = new DuenioDTO();
        dtoActualizado.setNombre("Juan");
        dtoActualizado.setApellido("Perez");
        dtoActualizado.setTelefono(123456);
        dtoActualizado.setEmail("juan@gmail.com");

        DuenioDTO respuestaDTO = new DuenioDTO();
        respuestaDTO.setId(1L);
        respuestaDTO.setNombre("Juan");
        respuestaDTO.setApellido("Perez");

        when(duenioRepository.findById(1L))
                .thenReturn(Optional.of(duenioExistente));

        when(duenioRepository.save(duenioExistente))
                .thenReturn(duenioExistente);

        when(duenioMapper.toDTO(duenioExistente))
                .thenReturn(respuestaDTO);

        // CUANDO
        DuenioDTO resultado =
                duenioService.updateDuenio(1L, dtoActualizado);

        // ENTONCES
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Perez");

        assertThat(duenioExistente.getNombre()).isEqualTo("Juan");
        assertThat(duenioExistente.getEmail())
                .isEqualTo("juan@gmail.com");

        verify(duenioRepository).findById(1L);
        verify(duenioRepository).save(duenioExistente);
    }
    @Test
    void deleteDuenio_cuandoExiste_eliminaDuenio() {

        // DADO
        Duenio duenio = new Duenio();
        duenio.setId(1L);
        duenio.setNombre("Carlos");

        when(duenioRepository.findById(1L))
                .thenReturn(Optional.of(duenio));

        // CUANDO
        duenioService.deleteDuenio(1L);

        // ENTONCES
        verify(duenioRepository).findById(1L);
        verify(duenioRepository).delete(duenio);
    }
    @Test
    void deleteDuenio_cuandoNoExiste_lanzaResourceNotFoundException() {

        // DADO
        when(duenioRepository.findById(99L))
                .thenReturn(Optional.empty());

        // CUANDO - ENTONCES
        assertThrows(
                ResourceNotFoundException.class,
                () -> duenioService.deleteDuenio(99L)
        );

        verify(duenioRepository).findById(99L);

        verify(duenioRepository, never())
                .delete(any(Duenio.class));
    }
}