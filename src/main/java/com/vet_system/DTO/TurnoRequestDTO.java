package com.vet_system.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha del turno no puede estar en el pasado")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    @NotNull(message = "La mascota es obligatoria")
    @Positive(message = "El ID de la mascota debe ser positivo")
    private Long mascotaId;

    @NotNull(message = "El veterinario es obligatorio")
    @Positive(message = "El ID del veterinario debe ser positivo")
    private Long veterinarioId;
}