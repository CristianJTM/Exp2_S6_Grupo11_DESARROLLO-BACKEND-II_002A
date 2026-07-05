package com.minimarket.util;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import org.springframework.stereotype.Component;

@Component
public class VentaValidator {

    public boolean usuarioValido(Usuario usuario) {

        return usuario != null
                && usuario.getUsername() != null
                && !usuario.getUsername().isBlank();
    }

    public boolean tieneStockSuficiente(Venta venta) {

        for (DetalleVenta detalle : venta.getDetalles()) {

            Producto producto = detalle.getProducto();

            if (producto.getStock() < detalle.getCantidad()) {
                return false;
            }
        }

        return true;
    }

    public double calcularTotal(Venta venta) {

        double total = 0;

        for (DetalleVenta detalle : venta.getDetalles()) {

            total += detalle.getPrecio()
                    * detalle.getCantidad();
        }

        return total;
    }

    public boolean usuarioPuedeRegistrarVenta(
            Usuario usuario) {

        return usuario.getRoles()
                .stream()
                .anyMatch(rol ->
                        rol.getNombre().equals("EMPLEADO")
                                || rol.getNombre().equals("ADMINISTRADOR"));
    }
}