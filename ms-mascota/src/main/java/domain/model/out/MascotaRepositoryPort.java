package domain.model.out;

import com.vet_system.Entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepositoryPort extends JpaRepository<Mascota, Long> {

    List<Mascota> findByDuenioId(Long duenioId);

    boolean existsByNombreAndDuenioId(String nombre, Long duenioId);

    long countByEspecie(String especie);
}