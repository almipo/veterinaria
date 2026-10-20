package com.veterinaria.veterinario.adapter.in.web.dto;

import com.veterinaria.veterinario.domain.model.Especialidad;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "La matricula es obligatoria")
    @Pattern(regexp = "MN-\\d{4}", message = "La matricula debe tener el formato MN-XXXX")
    private String matricula;

    @NotNull(message = "La especialidad es obligatoria")
    private Especialidad especialidad;

    @Email(message = "El email debe tener un formato valido")
    private String email;

    @Positive(message = "El telefono debe ser un numero positivo")
    private Integer telefono;
}
