package com.vet_system.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "veterinarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Veterinario
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(unique = true, nullable = false)
    private String matricula;



    @Enumerated(EnumType.STRING)
    @Column
    private Especialidad especialidad;


    @OneToMany(mappedBy = "veterinario",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <Turno> turnos;
}
