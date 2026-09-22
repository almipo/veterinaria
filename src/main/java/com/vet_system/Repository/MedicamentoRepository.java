package com.vet_system.Repository;

import com.vet_system.Entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository
        extends JpaRepository<Medicamento, Long> {
}