package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.service.CarritoService;
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
        name = "Carrito",
        description = "Operaciones relacionadas con el carrito de compras"
)
@RestController
@RequestMapping("/api/carrito")
@PreAuthorize("hasAnyRole('CLIENTE','CAJERO','ADMINISTRADOR')")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(
            summary = "Obtener todos los productos del carrito",
            description = "Devuelve la lista completa de productos en el carrito"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    public CollectionModel<EntityModel<Carrito>> listarCarrito() {

        List<EntityModel<Carrito>> carrito = carritoService.findAll()
                .stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(CarritoController.class)
                                .obtenerCarritoPorId(item.getId())).withSelfRel(),
                        linkTo(methodOn(CarritoController.class)
                                .listarCarrito()).withRel("carrito")
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(
                carrito,
                linkTo(methodOn(CarritoController.class)
                        .listarCarrito()).withSelfRel()
        );
    }

    @Operation(
            summary = "Obtener un carrito por ID",
            description = "Devuelve los detalles de un carrito específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorId(@PathVariable Long id) {

        Carrito carrito = carritoService.findById(id);

        if (carrito != null) {

            EntityModel<Carrito> resource = EntityModel.of(
                    carrito,
                    linkTo(methodOn(CarritoController.class)
                            .obtenerCarritoPorId(id)).withSelfRel(),
                    linkTo(methodOn(CarritoController.class)
                            .listarCarrito()).withRel("carrito"),
                    linkTo(methodOn(CarritoController.class)
                            .actualizarCarrito(id, carrito)).withRel("actualizar"),
                    linkTo(methodOn(CarritoController.class)
                            .eliminarProductoDelCarrito(id)).withRel("eliminar")
            );

            return ResponseEntity.ok(resource);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Agregar producto al carrito",
            description = "Agrega un producto al carrito de compras"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto agregado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Carrito>> agregarProductoAlCarrito(@RequestBody Carrito carrito) {

        Carrito nuevoCarrito = carritoService.save(carrito);

        EntityModel<Carrito> resource = EntityModel.of(
                nuevoCarrito,
                linkTo(methodOn(CarritoController.class)
                        .obtenerCarritoPorId(nuevoCarrito.getId())).withSelfRel(),
                linkTo(methodOn(CarritoController.class)
                        .listarCarrito()).withRel("carrito")
        );

        return ResponseEntity.created(
                linkTo(methodOn(CarritoController.class)
                        .obtenerCarritoPorId(nuevoCarrito.getId())).toUri()
        ).body(resource);
    }

    @Operation(
            summary = "Actualizar el carrito",
            description = "Modifica los datos del carrito"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> actualizarCarrito(
            @PathVariable Long id,
            @RequestBody Carrito carrito) {

        Carrito existente = carritoService.findById(id);

        if (existente != null) {

            carrito.setId(id);
            Carrito actualizado = carritoService.save(carrito);

            EntityModel<Carrito> resource = EntityModel.of(
                    actualizado,
                    linkTo(methodOn(CarritoController.class)
                            .obtenerCarritoPorId(id)).withSelfRel(),
                    linkTo(methodOn(CarritoController.class)
                            .listarCarrito()).withRel("carrito")
            );

            return ResponseEntity.ok(resource);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Eliminar un producto del carrito",
            description = "Elimina un producto ya registrado en el carrito"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito != null) {
            carritoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
