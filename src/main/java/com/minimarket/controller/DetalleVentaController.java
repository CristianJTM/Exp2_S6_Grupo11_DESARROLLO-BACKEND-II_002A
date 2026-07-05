package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.service.DetalleVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Detalles de Venta",
        description = "Operaciones relacionadas con los detalles de venta"
)
@RestController
@RequestMapping("/api/detalle-ventas")
@PreAuthorize("hasAnyRole('CAJERO','ADMINISTRADOR')")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Operation(
            summary = "Obtener todos los detalles de venta",
            description = "Devuelve la lista completa de detalles de venta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    public List<DetalleVenta> listarDetalleVentas() {
        return detalleVentaService.findAll();
    }

    @Operation(
            summary = "Obtener un detalle de venta por ID",
            description = "Devuelve los detalles de un detalle de venta específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de venta obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetalleVentaPorId(@PathVariable Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);
        return (detalleVenta != null) ? ResponseEntity.ok(detalleVenta) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Agregar detalle de venta",
            description = "Crea un nuevo detalle de venta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Detalle de venta agregado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    public DetalleVenta guardarDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        return detalleVentaService.save(detalleVenta);
    }

    @Operation(
            summary = "Actualizar detalle de venta",
            description = "Modifica los datos de un detalle de venta específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de venta actualizado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(@PathVariable Long id, @RequestBody DetalleVenta detalleVenta) {
        DetalleVenta existente = detalleVentaService.findById(id);
        if (existente != null) {
            detalleVenta.setId(id);
            return ResponseEntity.ok(detalleVentaService.save(detalleVenta));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Eliminar detalle de venta",
            description = "Elimina un detalle de venta específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de venta eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);
        if (detalleVenta != null) {
            detalleVentaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
