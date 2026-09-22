package com.vet_system.Mapper;

import com.vet_system.DTO.MedicamentoRequestDTO;
import com.vet_system.DTO.MedicamentoResponseDTO;
import com.vet_system.Entity.Medicamento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicamentoMapper {

    Medicamento toEntity(MedicamentoRequestDTO dto);

    MedicamentoResponseDTO toDTO(Medicamento medicamento);
}