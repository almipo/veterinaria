package com.vet_system.Repository;

import com.vet_system.Entity.Duenio;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface DuenioRepository extends JpaRepository<Duenio,Long> {

    boolean existsByCedula(String dni);

    Optional<Duenio> findByEmail(String email);
    List<Duenio> findByNombre(String nombre);

    List<Duenio> findByApellido(String apellido);

    List<Duenio> findByNombreAndApellido(String nombre, String apellido);

}
