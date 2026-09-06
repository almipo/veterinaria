package com.vet_system.DTO;

import com.vet_system.Entity.Especialidad;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "La matrícula es obligatoria")
    @Pattern(
            regexp = "MV-\\d{4}",
            message = "La matrícula debe tener el formato MV-XXXX"
    )
    private String matricula;

    @NotNull(message = "La especialidad es obligatoria")
    private Especialidad especialidad;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Positive(message = "El teléfono debe ser un número positivo")
    private Integer telefono;
}