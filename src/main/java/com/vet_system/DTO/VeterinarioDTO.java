package com.vet_system.DTO;

import com.vet_system.Entity.Especialidad;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de un veterinario")
public class VeterinarioDTO {
@Schema(description = "ID del veterinario", example = "1")
    private Long id;

@Schema(description = "Nombre", example = "Leandro")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

@Schema(description = "Apellido", example = "Perez")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

@Schema(description = "Matrícula profesional", example = "MN-4521")
    @NotBlank(message = "La matrícula es obligatoria")
    @Pattern(
            regexp = "MN-\\d{4}",
            message = "La matrícula debe tener el formato MN-XXXX"
    )
    private String matricula;

@Schema(description = "Especialidad", example = "Clínica General")
    @NotNull(message = "La especialidad es obligatoria")
    private Especialidad especialidad;

    @Schema(description = "Email", example = "leandro.perez@example.com")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Schema(description = "telefono", example = "123456789")

    @Positive(message = "El teléfono debe ser un número positivo")
    private Integer telefono;
}


