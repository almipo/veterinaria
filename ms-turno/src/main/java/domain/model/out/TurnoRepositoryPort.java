package domain.model.out;

import com.vet_system.Entity.Duenio;
import com.vet_system.Entity.Turno;
import com.vet_system.Service.DuenioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepositoryPort extends JpaRepository<Turno, Long> {

    // Evita dos turnos para el mismo veterinario
    // en la misma fecha y hora
    boolean existsByVeterinarioIdAndFechaAndHora(
            Long veterinarioId,
            LocalDate fecha,
            LocalTime hora
    );

    // Agenda de un veterinario para una fecha
    List<Turno> findByVeterinarioIdAndFecha(
            Long veterinarioId,
            LocalDate fecha
    );

    // Historial de turnos de una mascota
    List<Turno> findByMascotaIdOrderByFechaDescHoraDesc(
            Long mascotaId
    );
}