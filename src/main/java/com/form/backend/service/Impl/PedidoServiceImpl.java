package com.form.backend.service.Impl;

import com.form.backend.model.*;
import com.form.backend.repositorio.*;
import com.form.backend.service.PedidoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PreVentaRepository preVentaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CookieRepository cookieRepository;

    @Override
    @Transactional
    public Pedido registrarPedido(Pedido pedido) {
        PreVenta activa = preVentaRepository.findByActivoTrueOrderByIdDesc()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No hay pre-venta abierta."));

        pedido.setPreVenta(activa);

        Cliente clienteExistente = clienteRepository.findById(pedido.getCliente().getCelular()).orElse(null);

        if (clienteExistente != null) {
            clienteExistente.setGuardarDatos(pedido.getGuardarDatos());
            pedido.setCliente(clienteExistente);
        } else {
            pedido.getCliente().setGuardarDatos(pedido.getGuardarDatos());
        }

        if (pedido.getDetalles() != null) {
            pedido.getDetalles().forEach(detalle -> detalle.setPedido(pedido));
        }

        if (pedido.getCliente() != null) {
            clienteRepository.save(pedido.getCliente());
        }

        Pedido guardado = pedidoRepository.save(pedido);

        Long vendidosActualizados = pedidoRepository.countTotalCookiesByPreVentaId(activa.getId());
        if (vendidosActualizados >= activa.getStockMaximo()) {
            activa.setActivo(false);
            preVentaRepository.save(activa);
            List<Pedido> anulados = pedidoRepository.findByPreVentaIdAndAnuladoTrue(activa.getId());
            pedidoRepository.deleteAll(anulados);
        }

        return guardado;
    }

    @Override
    public List<Pedido> listarPorPreVenta(Long preVentaId) {
        return pedidoRepository.findByPreVentaId(preVentaId);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional
    public void toggleAnulacion(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        boolean nuevoEstado = !(pedido.getAnulado() != null && pedido.getAnulado());
        pedido.setAnulado(nuevoEstado);

        for (PedidoDetalle detalle : pedido.getDetalles()) {
            Cookie cookie = detalle.getCookie();
            int factor = nuevoEstado ? 1 : -1;

            if (cookie.getStockActual() == null) cookie.setStockActual(0);

            cookie.setStockActual(cookie.getStockActual() + (detalle.getCantidad() * factor));
            cookieRepository.save(cookie);
        }
        pedidoRepository.save(pedido);
    }

    @Override
    public void marcarComoVisto(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setVisto(true);
        pedidoRepository.save(pedido);
    }
}