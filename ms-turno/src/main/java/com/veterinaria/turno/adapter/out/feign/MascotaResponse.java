package com.veterinaria.turno.adapter.out.feign;

import lombok.Data;

@Data
public class MascotaResponse {
    private Long id;
    private String nombre;
    private String especie;
}
