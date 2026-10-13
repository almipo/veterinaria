package adapter.in.web.dto;


import com.vet_system.DTO.DuenioDTO;
import com.vet_system.Entity.Duenio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DuenioMapper {

    DuenioDTO toDTO(Duenio duenio){
        DuenioDTO dto = new DuenioDTO();
        dto.setId(duenio.getId());
        dto.setNombre(duenio.getNombre());
        dto.setApellido(duenio.getApellido());
        dto.setCedula(duenio.getCedula());
        dto.setTelefono(duenio.getTelefono());
        dto.setEmail(duenio.getEmail());
        return dto;
    };

    Duenio toEntity(DuenioDTO dto){
        duenio.setId(dto.getId());
        duenio.setNombre(dto.getNombre());
        duenio.setApellido(dto.getApellido());
        duenio.setCedula(dto.getCedula());
        duenio.setTelefono(dto.getTelefono());
        duenio.setEmail(dto.getEmail());
        return duenio;
    };
}