package com.form.backend.service;

import com.form.backend.model.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido registrarPedido(Pedido pedido);
    List<Pedido> listarPorPreVenta(Long preVentaId);
    List<Pedido> listarTodos();

    void toggleAnulacion(Long id);
    void marcarComoVisto(Long id);
}
