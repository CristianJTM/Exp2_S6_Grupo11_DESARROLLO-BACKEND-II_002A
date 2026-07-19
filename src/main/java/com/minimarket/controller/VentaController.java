package com.minimarket.controller;

import com.minimarket.assembler.VentaModelAssembler;
import com.minimarket.entity.Venta;
import com.minimarket.service.VentaService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Tag(
        name = "Ventas",
        description = "Operaciones relacionadas con las ventas de productos"
)
@RestController
@RequestMapping("/api/ventas")
@PreAuthorize("hasAnyRole('CAJERO','ADMINISTRADOR')")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaModelAssembler assembler;

    @Operation(
            summary = "Obtener todas las ventas",
            description = "Devuelve la lista completa de ventas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    public CollectionModel<EntityModel<Venta>> listarVentas() {

        List<EntityModel<Venta>> ventas =
                ventaService.findAll()
                        .stream()
                        .map(assembler::toModel)
                        .toList();

        return CollectionModel.of(
                ventas,
                linkTo(methodOn(VentaController.class)
                        .listarVentas())
                        .withSelfRel()
        );
    }

    @Operation(
            summary = "Obtener una venta por ID",
            description = "Devuelve los detalles de una venta específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venta>> obtenerVentaPorId(
            @PathVariable Long id) {

        Venta venta = ventaService.findById(id);

        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                assembler.toModel(venta)
        );
    }

    @Operation(
            summary = "Agregar venta",
            description = "Crea una nueva venta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venta agregada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Venta>> guardarVenta(
            @RequestBody Venta venta) {

        Venta nuevaVenta = ventaService.save(venta);

        return ResponseEntity.created(
                linkTo(methodOn(VentaController.class)
                        .obtenerVentaPorId(nuevaVenta.getId()))
                        .toUri()
        ).body(
                assembler.toModel(nuevaVenta)
        );
    }

    @Operation(
            summary = "Actualizar una venta",
            description = "Modifica los datos de una venta específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta actualizada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Venta>> actualizarVenta(
            @PathVariable Long id,
            @RequestBody Venta venta) {

        Venta existente = ventaService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        venta.setId(id);

        Venta actualizada = ventaService.save(venta);

        return ResponseEntity.ok(
                assembler.toModel(actualizada)
        );
    }

    @Operation(
            summary = "Eliminar venta",
            description = "Elimina una venta específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {

        Venta venta = ventaService.findById(id);

        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        ventaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
