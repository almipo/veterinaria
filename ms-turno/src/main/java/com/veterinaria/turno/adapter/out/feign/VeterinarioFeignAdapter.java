package com.veterinaria.turno.adapter.out.feign;

import com.veterinaria.turno.domain.port.out.VeterinarioServicePort;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.veterinaria.turno.domain.exception.ServicioNoDisponibleException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
@Component
@RequiredArgsConstructor
public class VeterinarioFeignAdapter implements VeterinarioServicePort {

    private final VeterinarioFeignClient client;

    @CircuitBreaker(
            name = "veterinarioService",
            fallbackMethod = "fallbackVeterinario"
    )
    @Retry(
            name = "veterinarioService",
            fallbackMethod = "fallbackVeterinario"
    )
    @Override
    public boolean existsById(Long id) {
        try {
            client.findById(id);
            return true;
        } catch (feign.FeignException.NotFound e) {
            return false;
        }
    }

    private boolean fallbackVeterinario(Long id, Throwable ex) {
        throw new ServicioNoDisponibleException(
                "El servicio de veterinarios no está disponible: "
                        + ex.getMessage()
        );
    }
}