package com.vet_system.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de un dueño")
public class DuenioDTO {

    @Schema(description = "ID del dueño", example = "1")
    private Long id;



    @Schema(description = "Nombre del dueño", example = "Carlos")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido del dueño", example = "Sanchez")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;


    @Schema(description = "Cédula o DNI del dueño", example = "31541741")
    @NotBlank(message = "La cedula es obligatorio")
    @Size(min = 7, max = 8, message = "El DNI debe tener entre 7 y 8 caracteres")
    private String cedula;


    @Schema(description = "Teléfono de contacto", example = "1123456789")
    @NotNull(message = "El teléfono es obligatorio")
    @Positive(message = "El teléfono debe ser positivo")
    private Integer telefono;

    @Schema(description = "Correo electrónico", example = "carlos@gmail.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;
}