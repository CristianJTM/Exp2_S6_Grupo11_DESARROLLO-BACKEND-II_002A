package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Tag(
        name = "Inventario",
        description = "Operaciones relacionadas con el inventario"
)
@RestController
@RequestMapping("/api/inventario")
@PreAuthorize("hasAnyRole('CAJERO','ADMINISTRADOR')")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Operation(
            summary = "Obtener todos los movimientos de inventario",
            description = "Devuelve la lista completa de movimientos de inventario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    public CollectionModel<EntityModel<Inventario>> listarMovimientosDeInventario() {

        List<EntityModel<Inventario>> movimientos = inventarioService.findAll()
                .stream()
                .map(inventario -> EntityModel.of(inventario,
                        linkTo(methodOn(InventarioController.class)
                                .obtenerMovimientoPorId(inventario.getId())).withSelfRel(),
                        linkTo(methodOn(InventarioController.class)
                                .listarMovimientosDeInventario()).withRel("inventario")
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(
                movimientos,
                linkTo(methodOn(InventarioController.class)
                        .listarMovimientosDeInventario()).withSelfRel()
        );
    }

    @Operation(
            summary = "Obtener un movimiento de inventario por ID",
            description = "Devuelve los detalles de un movimiento de inventario específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento de inventario obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> obtenerMovimientoPorId(@PathVariable Long id) {

        Inventario inventario = inventarioService.findById(id);

        if (inventario != null) {

            EntityModel<Inventario> resource = EntityModel.of(
                    inventario,
                    linkTo(methodOn(InventarioController.class)
                            .obtenerMovimientoPorId(id)).withSelfRel(),
                    linkTo(methodOn(InventarioController.class)
                            .listarMovimientosDeInventario()).withRel("inventario"),
                    linkTo(methodOn(InventarioController.class)
                            .actualizarMovimiento(id, inventario)).withRel("actualizar"),
                    linkTo(methodOn(InventarioController.class)
                            .eliminarMovimiento(id)).withRel("eliminar")
            );

            return ResponseEntity.ok(resource);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Agregar movimiento de inventario",
            description = "Crea un nuevo movimiento de inventario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento de inventario agregado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Inventario>> registrarMovimiento(@RequestBody Inventario inventario) {

        Inventario nuevoMovimiento = inventarioService.save(inventario);

        EntityModel<Inventario> resource = EntityModel.of(
                nuevoMovimiento,
                linkTo(methodOn(InventarioController.class)
                        .obtenerMovimientoPorId(nuevoMovimiento.getId())).withSelfRel(),
                linkTo(methodOn(InventarioController.class)
                        .listarMovimientosDeInventario()).withRel("inventario")
        );

        return ResponseEntity.created(
                linkTo(methodOn(InventarioController.class)
                        .obtenerMovimientoPorId(nuevoMovimiento.getId())).toUri()
        ).body(resource);
    }

    @Operation(
            summary = "Actualizar movimiento de inventario",
            description = "Modifica los datos de un movimiento de inventario específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento de inventario actualizado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Movimiento de inventario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> actualizarMovimiento(
            @PathVariable Long id,
            @RequestBody Inventario inventario) {

        Inventario existente = inventarioService.findById(id);

        if (existente != null) {

            inventario.setId(id);
            Inventario actualizado = inventarioService.save(inventario);

            EntityModel<Inventario> resource = EntityModel.of(
                    actualizado,
                    linkTo(methodOn(InventarioController.class)
                            .obtenerMovimientoPorId(id)).withSelfRel(),
                    linkTo(methodOn(InventarioController.class)
                            .listarMovimientosDeInventario()).withRel("inventario")
            );

            return ResponseEntity.ok(resource);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Eliminar movimiento de inventario",
            description = "Elimina un movimiento de inventario específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento de inventario eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Movimiento de inventario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
