package com.vet_system.DTO;

import com.vet_system.Entity.Turno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.vet_system.Entity.EstadoTurno;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponseDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;

    private EstadoTurno estado;
    private String observaciones;

    private Long mascotaId;
    private String mascotaNombre;

    private Long veterinarioId;
    private String veterinarioNombre;
}