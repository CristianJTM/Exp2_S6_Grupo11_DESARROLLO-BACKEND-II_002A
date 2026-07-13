package com.minimarket.controller;

import com.minimarket.entity.Usuario;
import com.minimarket.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "Usuarios",
        description = "Operaciones relacionadas con los usuarios"
)
@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasAnyRole('CAJERO','ADMINISTRADOR')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve la lista completa de usuarios"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {

        List<EntityModel<Usuario>> usuarios = usuarioService.findAll()
                .stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioController.class)
                                .obtenerUsuarioPorId(usuario.getId())).withSelfRel(),
                        linkTo(methodOn(UsuarioController.class)
                                .listarUsuarios()).withRel("usuarios")
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class)
                        .listarUsuarios()).withSelfRel());
    }

    @Operation(
            summary = "Obtener un usuario por ID",
            description = "Devuelve los detalles de un usuario específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorId(@PathVariable Long id) {

        Optional<Usuario> usuario = usuarioService.findById(id);

        if (usuario.isPresent()) {

            EntityModel<Usuario> resource = EntityModel.of(usuario.get(),
                    linkTo(methodOn(UsuarioController.class)
                            .obtenerUsuarioPorId(id)).withSelfRel(),
                    linkTo(methodOn(UsuarioController.class)
                            .listarUsuarios()).withRel("usuarios"),
                    linkTo(methodOn(UsuarioController.class)
                            .actualizarUsuario(id, usuario.get())).withRel("actualizar"),
                    linkTo(methodOn(UsuarioController.class)
                            .eliminarUsuario(id)).withRel("eliminar")
            );

            return ResponseEntity.ok(resource);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un nuevo usuario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> guardarUsuario(@RequestBody Usuario usuario) {

        Usuario nuevoUsuario = usuarioService.save(usuario);

        EntityModel<Usuario> resource = EntityModel.of(nuevoUsuario,
                linkTo(methodOn(UsuarioController.class)
                        .obtenerUsuarioPorId(nuevoUsuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class)
                        .listarUsuarios()).withRel("usuarios"));

        return ResponseEntity.created(
                        linkTo(methodOn(UsuarioController.class)
                                .obtenerUsuarioPorId(nuevoUsuario.getId())).toUri())
                .body(resource);
    }

    @Operation(
            summary = "Actualizar un usuario",
            description = "Modifica los datos de un usuario específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        Optional<Usuario> usuarioExistente = usuarioService.findById(id);

        if (usuarioExistente.isPresent()) {

            usuario.setId(id);
            Usuario actualizado = usuarioService.save(usuario);

            EntityModel<Usuario> resource = EntityModel.of(actualizado,
                    linkTo(methodOn(UsuarioController.class)
                            .obtenerUsuarioPorId(id)).withSelfRel(),
                    linkTo(methodOn(UsuarioController.class)
                            .listarUsuarios()).withRel("usuarios"));

            return ResponseEntity.ok(resource);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) { // Verifica si el usuario existe
            usuarioService.deleteById(id); // Elimina al usuario
            return ResponseEntity.noContent().build(); // Respuesta 204 (sin contenido)
        }
        return ResponseEntity.notFound().build(); // Respuesta 404 (no encontrado)
    }
}
