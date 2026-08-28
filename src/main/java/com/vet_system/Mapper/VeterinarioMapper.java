package com.vet_system.Mapper;

import com.vet_system.DTO.VeterinarioDTO;
import com.vet_system.Entity.Veterinario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeterinarioMapper {

    VeterinarioDTO toDTO(Veterinario veterinario);

    Veterinario toEntity(VeterinarioDTO dto);
}