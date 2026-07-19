package com.minimarket.service.impl;

import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.VentaService;
import com.minimarket.util.VentaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaValidator ventaValidator;

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta findById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Venta save(Venta venta) {
        if (!ventaValidator.usuarioValido(
                venta.getUsuario())) {

            throw new RuntimeException(
                    "Usuario inválido");
        }

        if (!ventaValidator.tieneStockSuficiente(
                venta)) {

            throw new RuntimeException(
                    "Stock insuficiente");
        }

        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> findByUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }
}
