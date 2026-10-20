package com.veterinaria.turno.adapter.in.web.dto;

import com.veterinaria.turno.domain.model.Turno;

import java.time.LocalDateTime;

public final class TurnoMapper {

    private TurnoMapper() {
    }

    public static Turno toEntity(TurnoRequest request) {
        Turno turno = new Turno();
        turno.setFechaHora(LocalDateTime.of(request.getFecha(), request.getHora()));
        turno.setMotivo(request.getMotivo());
        turno.setMascotaId(request.getMascotaId());
        turno.setVeterinarioId(request.getVeterinarioId());
        return turno;
    }

    public static TurnoResponse toResponse(Turno turno) {
        return new TurnoResponse(
                turno.getId(),
                turno.getFechaHora().toLocalDate(),
                turno.getFechaHora().toLocalTime(),
                turno.getMotivo(),
                turno.getEstado(),
                turno.getObservaciones(),
                turno.getMascotaId(),
                turno.getVeterinarioId()
        );
    }
}
