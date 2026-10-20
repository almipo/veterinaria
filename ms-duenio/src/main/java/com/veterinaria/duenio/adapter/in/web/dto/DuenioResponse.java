package com.veterinaria.duenio.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de un dueño")
public class DuenioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String cedula;
    private Integer telefono;
    private String email;
}
