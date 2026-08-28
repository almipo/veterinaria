package com.vet_system.DTO;

import com.vet_system.Entity.Especialidad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String matricula;
    private Especialidad especialidad;
    private String email;
    private Integer telefono;
}