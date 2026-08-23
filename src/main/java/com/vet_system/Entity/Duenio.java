package com.vet_system.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "duenio")
@Getter
@Setter

public class Duenio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String nombre;

    @Column( nullable = false)
    private String apellido;

    @Column( nullable = false, unique = true)
    private String cedula;

    @Column( nullable = false)
    private Integer telefono;

    @Column( nullable = false)
    private  String email;

    @OneToMany(mappedBy="duenio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Mascota> mascotasList;


}
