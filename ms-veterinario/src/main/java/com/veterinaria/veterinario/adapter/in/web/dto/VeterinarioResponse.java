package com.veterinaria.veterinario.adapter.in.web.dto;

import com.veterinaria.veterinario.domain.model.Especialidad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String matricula;
    private Especialidad especialidad;
    private String email;
    private Integer telefono;
}
