package com.minimarket.assembler;

import com.minimarket.controller.DetalleVentaController;
import com.minimarket.entity.DetalleVenta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DetalleVentaModelAssembler implements RepresentationModelAssembler<DetalleVenta, EntityModel<DetalleVenta>> {

    @Override
    public EntityModel<DetalleVenta> toModel(DetalleVenta detalleVenta) {

        return EntityModel.of(
                detalleVenta,

                linkTo(methodOn(DetalleVentaController.class)
                        .obtenerDetalleVentaPorId(detalleVenta.getId()))
                        .withSelfRel(),

                linkTo(methodOn(DetalleVentaController.class)
                        .listarDetalleVentas())
                        .withRel("detalle-ventas"),

                linkTo(methodOn(DetalleVentaController.class)
                        .actualizarDetalleVenta(detalleVenta.getId(), detalleVenta))
                        .withRel("actualizar"),

                linkTo(methodOn(DetalleVentaController.class)
                        .eliminarDetalleVenta(detalleVenta.getId()))
                        .withRel("eliminar")
        );
    }
}
