package com.vet_system.Mapper;

import com.vet_system.DTO.DuenioDTO;
import com.vet_system.Entity.Duenio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DuenioMapper {

    DuenioDTO toDTO(Duenio duenio);

    Duenio toEntity(DuenioDTO dto);
}