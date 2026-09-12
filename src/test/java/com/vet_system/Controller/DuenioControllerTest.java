package com.vet_system.Controller;

import com.vet_system.DTO.DuenioDTO;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Service.DuenioService;
import com.vet_system.Service.MascotaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DuenioController.class)
class DuenioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DuenioService duenioService;

    @MockitoBean
    private MascotaService mascotaService;



    // TEST 1 - GET lista
    @Test
    void listarTodos_retorna200() throws Exception {

        // DADO
        when(duenioService.listarTodos())
                .thenReturn(List.of());

        // CUANDO - ENTONCES
        mockMvc.perform(get("/api/duenio"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    // TEST 2 - GET por ID exitoso
    @Test
    void buscarPorId_cuandoExiste_retorna200() throws Exception {

        // DADO
        DuenioDTO dto = new DuenioDTO();
        dto.setId(1L);
        dto.setNombre("Carlos");
        dto.setApellido("Sanchez");
        dto.setCedula("31541741");
        dto.setEmail("carlos@gmail.com");

        when(duenioService.buscarPorId(1L))
                .thenReturn(dto);

        // CUANDO - ENTONCES
        mockMvc.perform(get("/api/duenio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.apellido").value("Sanchez"));
    }


    // TEST 3 - GET por ID inexistente
    @Test
    void buscarPorId_cuandoNoExiste_retorna404() throws Exception {

        // DADO
        when(duenioService.buscarPorId(99L))
                .thenThrow(
                        new ResourceNotFoundException("Duenio", 99L)
                );

        // CUANDO - ENTONCES
        mockMvc.perform(get("/api/duenio/99"))
                .andExpect(status().isNotFound());
    }


    // TEST 4 - POST válido
    @Test
    void registrarDuenio_conDatosValidos_retorna201() throws Exception {

        // DADO
        DuenioDTO respuesta = new DuenioDTO();
        respuesta.setId(1L);
        respuesta.setNombre("Carlos");
        respuesta.setApellido("Sanchez");
        respuesta.setCedula("31541741");
        respuesta.setEmail("carlos@gmail.com");

        when(duenioService.registrarDuenio(any(DuenioDTO.class)))
                .thenReturn(respuesta);

        String json = """
                {
                    "nombre": "Carlos",
                    "apellido": "Sanchez",
                    "cedula": "31541741",
                    "telefono": 123456789,
                    "email": "carlos@gmail.com"
                }
                """;

        // CUANDO - ENTONCES
        mockMvc.perform(post("/api/duenio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }


    // TEST 5 - POST inválido
    @Test
    void registrarDuenio_conEmailInvalido_retorna400() throws Exception {

        // DADO
        String json = """
                {
                    "nombre": "Carlos",
                    "apellido": "Sanchez",
                    "cedula": "31541741",
                    "telefono": 123456789,
                    "email": "email-invalido"
                }
                """;

        // CUANDO - ENTONCES
        mockMvc.perform(post("/api/duenio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}



