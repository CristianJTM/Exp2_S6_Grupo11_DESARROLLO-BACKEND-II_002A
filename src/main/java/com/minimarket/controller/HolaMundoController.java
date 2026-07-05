package com.minimarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Hola Mundo",
        description = "Operaciones relacionadas con el mensaje de saludo"
)
@RestController
public class HolaMundoController {

    @Operation(
            summary = "Decir hola",
            description = "Devuelve un mensaje de saludo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mensaje obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/public/hola")
    public String holaMundo() {
        return "¡Hola Mundo!";
    }
}
