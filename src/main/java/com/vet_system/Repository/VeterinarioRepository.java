package com.vet_system.Repository;

import com.vet_system.Entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    boolean existsByMatricula(String matricula);

    Optional<Veterinario> findByMatricula(String matricula);
}