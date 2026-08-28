package com.vet_system.Entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Mascotas")
@Getter
@Setter



public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String nombre;

    @Column( nullable = false)
    private String especie;

    @Column( nullable = false)
    private String raza;

    @Column(nullable = false )
    private LocalDate fechaNacimiento;


    @ManyToOne(  fetch = FetchType.LAZY)
    @JoinColumn(name = "duenioId", nullable = false)

    private Duenio duenio;





}
