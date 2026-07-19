package com.minimarket.controller;

import com.minimarket.assembler.CategoriaModelAssembler;
import com.minimarket.entity.Categoria;
import com.minimarket.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Tag(
        name = "Categorías",
        description = "Operaciones relacionadas con las categorías de productos"
)
@RestController
@RequestMapping("/api/categorias")
@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaModelAssembler assembler;

    @Operation(
            summary = "Obtener todas las categorías",
            description = "Devuelve la lista completa de categorías"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    public CollectionModel<EntityModel<Categoria>> listarCategorias() {
        List<EntityModel<Categoria>> categorias = categoriaService.findAll()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(
                categorias,
                linkTo(methodOn(CategoriaController.class)
                        .listarCategorias())
                        .withSelfRel()
        );
    }

    @Operation(
            summary = "Obtener una categoría por ID",
            description = "Devuelve los detalles de una categoría específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Categoria>> obtenerCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);

        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                assembler.toModel(categoria)
        );
    }

    @Operation(
            summary = "Agregar categoría",
            description = "Crea una nueva categoría"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría agregada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Categoria>> guardarCategoria(@RequestBody Categoria categoria) {

        Categoria nuevaCategoria = categoriaService.save(categoria);

        return ResponseEntity.created(
                linkTo(methodOn(CategoriaController.class)
                        .obtenerCategoriaPorId(nuevaCategoria.getId()))
                        .toUri()
        ).body(
                assembler.toModel(nuevaCategoria)
        );
    }

    @Operation(
            summary = "Actualizar categoría",
            description = "Modifica los datos de una categoría específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Categoria>> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria existente = categoriaService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        categoria.setId(id);

        Categoria actualizada = categoriaService.save(categoria);

        return ResponseEntity.ok(
                assembler.toModel(actualizada)
        );
    }

    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);

        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }

        categoriaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
