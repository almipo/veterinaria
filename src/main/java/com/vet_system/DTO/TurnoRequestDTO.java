package com.vet_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos necesarios para registrar un turno")
public class TurnoRequestDTO {

@Schema(description = "Fecha del turno", example = "2026-10-15")
    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha del turno no puede estar en el pasado")
    private LocalDate fecha;

@Schema(description = "Hora del turno", example = "10:30:00")
    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

@Schema(description = "Motivo de la consulta", example = "Control anual")
    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

@Schema(description = "ID de la mascota", example = "1")
    @NotNull(message = "La mascota es obligatoria")
    @Positive(message = "El ID de la mascota debe ser positivo")
    private Long mascotaId;

@Schema(description = "ID del veterinario", example = "2")
    @NotNull(message = "El veterinario es obligatorio")
    @Positive(message = "El ID del veterinario debe ser positivo")
    private Long veterinarioId;
}

