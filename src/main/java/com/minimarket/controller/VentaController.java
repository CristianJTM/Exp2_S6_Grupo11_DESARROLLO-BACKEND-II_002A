package com.minimarket.controller;

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
    public List<Venta> listarVentas() {
        return ventaService.findAll();
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
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Long id) {
        Venta venta = ventaService.findById(id);
        return (venta != null) ? ResponseEntity.ok(venta) : ResponseEntity.notFound().build();
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
    public Venta guardarVenta(@RequestBody Venta venta) {
        return ventaService.save(venta);
    }
}
