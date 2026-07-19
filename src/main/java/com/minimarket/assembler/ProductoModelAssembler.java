package com.minimarket.assembler;

import com.minimarket.controller.CategoriaController;
import com.minimarket.controller.ProductoController;
import com.minimarket.entity.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {

        EntityModel<Producto> model = EntityModel.of(producto);

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .obtenerProductoPorId(producto.getId()))
                        .withSelfRel());

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withRel("productos"));

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .actualizarProducto(producto.getId(), producto))
                        .withRel("actualizar"));

        model.add(
                linkTo(methodOn(ProductoController.class)
                        .eliminarProducto(producto.getId()))
                        .withRel("eliminar"));

        if(producto.getCategoria()!=null){

            model.add(
                    linkTo(methodOn(CategoriaController.class)
                            .obtenerCategoriaPorId(producto.getCategoria().getId()))
                            .withRel("categoria"));
        }

        return model;
    }

}
