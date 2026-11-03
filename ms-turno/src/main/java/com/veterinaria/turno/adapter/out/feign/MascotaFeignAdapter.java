package com.veterinaria.turno.adapter.out.feign;

import com.veterinaria.turno.domain.port.out.MascotaServicePort;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import com.veterinaria.turno.domain.exception.ServicioNoDisponibleException;

@Component
@RequiredArgsConstructor
public class MascotaFeignAdapter implements MascotaServicePort {

    private final MascotaFeignClient client;

    @CircuitBreaker(name = "mascotaService", fallbackMethod = "fallbackMascota")
    @Retry(name = "mascotaService", fallbackMethod = "fallbackMascota")
    @Override
    public boolean existsById(Long id) {
        try {
            client.findById(id);
            return true;
        } catch (feign.FeignException.NotFound e) {
            return false;
        }
    }
    private boolean fallbackMascota(Long id, Throwable ex) {
        throw new ServicioNoDisponibleException(
                "El servicio de mascotas no está disponible: " + ex.getMessage()
        );
    }
}
