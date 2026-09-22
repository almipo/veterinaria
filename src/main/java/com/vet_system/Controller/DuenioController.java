package com.vet_system.Controller;

import com.vet_system.DTO.DuenioDTO;
import com.vet_system.DTO.MascotaDTO;
import com.vet_system.Service.DuenioService;
import com.vet_system.Service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/duenio")
@RequiredArgsConstructor
@Tag(
        name = "Dueños",
        description = "Operaciones para gestionar los dueños de las mascotas"
)
public class DuenioController {

    private final DuenioService duenioService;
    private final MascotaService mascotaService;

    @Operation(
            summary = "Registrar un dueño",
            description = "Crea un nuevo dueño en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dueño creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Dueño duplicado")
    })
    @PostMapping
    public ResponseEntity<DuenioDTO> registrarDuenio(
            @Valid @RequestBody DuenioDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(duenioService.registrarDuenio(dto));
    }


    @Operation(
            summary = "Buscar dueño por ID",
            description = "Obtiene un dueño utilizando su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dueño encontrado"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DuenioDTO> buscarPorId(   @Parameter(
            description = "ID del dueño",
            example = "1"
    )@PathVariable Long id) {
        return ResponseEntity.ok(
                duenioService.buscarPorId(id)
        );
    }

    @Operation(
            summary = "Listar todos los dueños",
            description = "Obtiene una lista de todos los dueños en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dueños encontrados")
    })
    @GetMapping
    public ResponseEntity<List<DuenioDTO>> listarTodos() {
        return ResponseEntity.ok(
                duenioService.listarTodos()
        );
    }

    @Operation(
            summary = "Actualizar dueño",
            description = "Actualiza la información de un dueño existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dueño actualizado"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DuenioDTO> actualizarDuenio(@Parameter(description = "ID del dueño", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody DuenioDTO dto) {

        return ResponseEntity.ok(
                duenioService.updateDuenio(id, dto)
        );
    }

    @Operation(summary = "Eliminar dueño",
            description = "Elimina un dueño del sistema utilizando su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dueño eliminado"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDuenio(@Parameter(description = "ID del dueño", example = "1")
            @PathVariable Long id) {

        duenioService.deleteDuenio(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar dueño por email",
            description = "Obtiene un dueño utilizando su dirección de correo electrónico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dueño encontrado"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@Parameter(
            description = "Email del dueño",
            example = "juan.perez@gmail.com"
    )@PathVariable String email) {
        return duenioService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @Operation(
            summary = "Buscar dueño por nombre",
            description = "Obtiene una lista de dueños que coinciden con el nombre proporcionado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dueños encontrados"),
            @ApiResponse(responseCode = "404", description = "Dueños no encontrados")
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<DuenioDTO>> buscarPorNombre( @Parameter(
            description = "Nombre del dueño",
            example = "Juan"
    )
            @PathVariable String nombre) {

        return ResponseEntity.ok(
                duenioService.buscarPorNombre(nombre)
        );
    }


    @Operation(
            summary = "Buscar dueño por apellido",
            description = "Obtiene una lista de dueños que coinciden con el apellido proporcionado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dueños encontrados"),
            @ApiResponse(responseCode = "404", description = "Dueños no encontrados")
    })
    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<DuenioDTO>> buscarPorApellido(@Parameter(
            description = "Apellido del dueño",
            example = "Perez"
    )
            @PathVariable String apellido) {

        return ResponseEntity.ok(
                duenioService.buscarPorApellido(apellido)
        );
    }

    @Operation(
            summary = "Buscar dueño por nombre y apellido",
            description = "Obtiene una lista de dueños que coinciden con el nombre y apellido proporcionados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dueños encontrados"),
            @ApiResponse(responseCode = "404", description = "Dueños no encontrados")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<DuenioDTO>> buscarPorNombreYApellido(
            @RequestParam String nombre,
            @RequestParam String apellido) {

        return ResponseEntity.ok(
                duenioService.buscarPorNombreYApellido(nombre, apellido)
        );
    }

    @Operation(
            summary = "Obtener mascotas por ID dueño",
            description = "Obtiene una lista de mascotas asociadas a un dueño específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascotas encontradas"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado")
    })
    @GetMapping("/{id}/mascotas")
    public ResponseEntity<List<MascotaDTO>> getMascotasByDuenio( @Parameter(
            description = "ID del dueño",
            example = "1"
    )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                mascotaService.getMascotasByDuenio(id)
        );
    }
}