package com.minimarket.controller;

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
                        .map(producto -> EntityModel.of(
                                producto,

                                linkTo(methodOn(ProductoController.class)
                                        .obtenerProductoPorId(producto.getId()))
                                        .withSelfRel(),

                                linkTo(methodOn(ProductoController.class)
                                        .listarProductos())
                                        .withRel("productos"),

                                linkTo(methodOn(CategoriaController.class)
                                        .obtenerCategoriaPorId(producto.getCategoria().getId()))
                                        .withRel("categoria")
                        ))
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
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(
            @PathVariable Long id) {

        Producto producto = productoService.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Producto> model =
                EntityModel.of(producto);

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .obtenerProductoPorId(id))
                        .withSelfRel());

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withRel("productos"));

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .actualizarProducto(id, producto))
                        .withRel("actualizar")
        );

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .eliminarProducto(id))
                        .withRel("eliminar")
        );

        model.add(
                linkTo(methodOn(CategoriaController.class)
                        .obtenerCategoriaPorId(producto.getCategoria().getId()))
                        .withRel("categoria")
        );

        return ResponseEntity.ok(model);
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
            @RequestBody Producto producto) {

        Producto nuevo =
                productoService.save(producto);

        EntityModel<Producto> model =
                EntityModel.of(nuevo);

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .obtenerProductoPorId(nuevo.getId()))
                        .withSelfRel());

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withRel("productos"));

        return ResponseEntity
                .status(201)
                .body(model);
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
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoExistente = productoService.findById(id);
        if (productoExistente != null) {
            producto.setId(id);
            return ResponseEntity.ok(productoService.save(producto));
        }
        return ResponseEntity.notFound().build();
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
