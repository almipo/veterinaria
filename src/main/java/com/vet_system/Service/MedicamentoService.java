package com.vet_system.Service;

import com.vet_system.DTO.MedicamentoRequestDTO;
import com.vet_system.DTO.MedicamentoResponseDTO;
import com.vet_system.Entity.Medicamento;
import com.vet_system.Exception.ResourceNotFoundException;
import com.vet_system.Mapper.MedicamentoMapper;
import com.vet_system.Repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final MedicamentoMapper medicamentoMapper;

    public List<MedicamentoResponseDTO> getAllMedicamentos() {

        return medicamentoRepository.findAll()
                .stream()
                .map(medicamentoMapper::toDTO)
                .toList();
    }

    public MedicamentoResponseDTO getMedicamentoById(Long id) {

        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicamento", id));

        return medicamentoMapper.toDTO(medicamento);
    }

    public MedicamentoResponseDTO createMedicamento(MedicamentoRequestDTO dto) {

        Medicamento medicamento = medicamentoMapper.toEntity(dto);

        Medicamento medicamentoGuardado =
                medicamentoRepository.save(medicamento);

        return medicamentoMapper.toDTO(medicamentoGuardado);
    }

    public MedicamentoResponseDTO updateMedicamento(
            Long id,
            MedicamentoRequestDTO dto) {

        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicamento", id));

        medicamento.setNombre(dto.getNombre());
        medicamento.setPrincipioActivo(dto.getPrincipioActivo());
        medicamento.setStock(dto.getStock());
        medicamento.setPrecioUnitario(dto.getPrecioUnitario());

        return medicamentoMapper.toDTO(
                medicamentoRepository.save(medicamento)
        );
    }

    public void deleteMedicamento(Long id) {

        if (!medicamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicamento", id);
        }

        medicamentoRepository.deleteById(id);
    }
}