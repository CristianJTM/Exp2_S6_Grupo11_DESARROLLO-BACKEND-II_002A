package com.minimarket.controller;

import com.minimarket.assembler.ProductoModelAssembler;
import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Productos",
        description = "Operaciones relacionadas con los productos del minimarket"
)
@RestController
@RequestMapping("/api/productos")
@PreAuthorize("hasAnyRole('CAJERO','ADMINISTRADOR')")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @Operation(
            summary = "Obtener todos los productos",
            description = "Devuelve la lista completa de productos registrados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    public CollectionModel<EntityModel<Producto>> listarProductos() {

        List<EntityModel<Producto>> productos =
                productoService.findAll()
                        .stream()
                        .map(assembler::toModel)
                        .toList();

        return CollectionModel.of(
                productos,
                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withSelfRel()
        );
    }

    @Operation(
            summary = "Obtener un producto por ID",
            description = "Devuelve los detalles de un producto específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(@PathVariable Long id){

        Producto producto = productoService.findById(id);

        if(producto==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                assembler.toModel(producto)
        );
    }

    @PostMapping
    @Operation(
            summary = "Guardar un nuevo producto",
            description = "Crea un nuevo producto en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<EntityModel<Producto>> guardarProducto(
            @RequestBody Producto producto){

        Producto nuevo = productoService.save(producto);

        return ResponseEntity
                .created(
                        linkTo(methodOn(ProductoController.class)
                                .obtenerProductoPorId(nuevo.getId()))
                                .toUri()
                )
                .body(
                        assembler.toModel(nuevo)
                );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un producto",
            description = "Modifica los datos de un producto ya registrado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto){

        Producto existente = productoService.findById(id);

        if(existente==null){
            return ResponseEntity.notFound().build();
        }

        producto.setId(id);

        Producto actualizado =
                productoService.save(producto);

        return ResponseEntity.ok(
                assembler.toModel(actualizado)
        );
    }

    @Operation(
            summary = "Eliminar un producto existente",
            description = "Elimina un producto ya registrado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto != null) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
