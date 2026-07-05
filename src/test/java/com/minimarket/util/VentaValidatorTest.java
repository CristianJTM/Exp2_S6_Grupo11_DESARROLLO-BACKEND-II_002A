package com.minimarket.util;

import com.minimarket.entity.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VentaValidatorTest {

    @Mock
    private final VentaValidator validator =
            new VentaValidator();

    @Test
    void usuarioValido_DebeRetornarTrue() {

        Usuario usuario = new Usuario();

        usuario.setUsername("cristian");

        assertTrue(
                validator.usuarioValido(usuario)
        );
    }

    @Test
    void usuarioSinUsername_DebeRetornarFalse() {

        Usuario usuario = new Usuario();

        assertFalse(
                validator.usuarioValido(usuario)
        );
    }

    @Test
    void stockSuficiente_DebeRetornarTrue() {

        Producto producto = new Producto();

        producto.setStock(20);

        DetalleVenta detalle =
                new DetalleVenta();

        detalle.setCantidad(5);
        detalle.setProducto(producto);

        Venta venta = new Venta();

        venta.setDetalles(
                List.of(detalle)
        );

        assertTrue(
                validator.tieneStockSuficiente(
                        venta
                )
        );
    }

    @Test
    void stockInsuficiente_DebeRetornarFalse() {

        Producto producto = new Producto();

        producto.setStock(2);

        DetalleVenta detalle =
                new DetalleVenta();

        detalle.setCantidad(5);
        detalle.setProducto(producto);

        Venta venta = new Venta();

        venta.setDetalles(
                List.of(detalle)
        );

        assertFalse(
                validator.tieneStockSuficiente(
                        venta
                )
        );
    }

    @Test
    void calcularTotal_DebeRetornarValorCorrecto() {

        DetalleVenta detalle =
                new DetalleVenta();

        detalle.setCantidad(2);
        detalle.setPrecio(1000.0);

        Venta venta = new Venta();

        venta.setDetalles(
                List.of(detalle)
        );

        double total =
                validator.calcularTotal(venta);

        assertEquals(
                2000.0,
                total
        );
    }

    @Test
    void cajeroPuedeRegistrarVenta() {

        Rol rol = new Rol();

        rol.setNombre("CAJERO");

        Usuario usuario = new Usuario();

        usuario.setRoles(
                Set.of(rol)
        );

        assertTrue(
                validator.usuarioPuedeRegistrarVenta(
                        usuario
                )
        );
    }

    @Test
    void administradorPuedeRegistrarVenta() {

        Rol rol = new Rol();

        rol.setNombre("ADMINISTRADOR");

        Usuario usuario = new Usuario();

        usuario.setRoles(
                Set.of(rol)
        );

        assertTrue(
                validator.usuarioPuedeRegistrarVenta(
                        usuario
                )
        );
    }

    @Test
    void clienteNoPuedeRegistrarVenta() {

        Rol rol = new Rol();

        rol.setNombre("CLIENTE");

        Usuario usuario = new Usuario();

        usuario.setRoles(
                Set.of(rol)
        );

        assertFalse(
                validator.usuarioPuedeRegistrarVenta(
                        usuario
                )
        );
    }
}