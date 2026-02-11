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
        // 1. Obtener la preventa activa
        PreVenta activa = preVentaRepository.findByActivoTrueOrderByIdDesc()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No hay pre-venta abierta."));

        pedido.setPreVenta(activa);

        // 2. Lógica de Cliente (Persistencia de datos)
        Cliente clienteInput = pedido.getCliente();
        Cliente clienteExistente = clienteRepository.findById(clienteInput.getCelular()).orElse(null);

        if (clienteExistente != null) {
            clienteExistente.setNombre(clienteInput.getNombre());
            clienteExistente.setApellido(clienteInput.getApellido());
            clienteExistente.setGuardarDatos(pedido.getGuardarDatos());
            pedido.setCliente(clienteExistente);
        } else {
            pedido.getCliente().setGuardarDatos(pedido.getGuardarDatos());
        }

        // 3. Lógica de Logística (Delivery vs Recojo)
        if ("DELIVERY".equals(pedido.getTipoEntrega()) && pedido.getDireccion() != null) {
            pedido.getDireccion().setId(null);
        } else if ("RECOJO".equals(pedido.getTipoEntrega())) {
            pedido.setDireccion(null);
            pedido.setCostoEnvio(0.0);
        }

        // 4. Vincular detalles al pedido
        if (pedido.getDetalles() != null) {
            pedido.getDetalles().forEach(detalle -> detalle.setPedido(pedido));
        }

        // 5. Guardar cliente y pedido
        if (pedido.getCliente() != null) {
            clienteRepository.save(pedido.getCliente());
        }

        Pedido guardado = pedidoRepository.save(pedido);


        int galletasVendidasEnEstePedido = pedido.getDetalles().stream()
                .mapToInt(d -> Boolean.TRUE.equals(d.getEsPack()) ? d.getCantidad() * 4 : d.getCantidad())
                .sum();

        activa.setStockActual(activa.getStockActual() + galletasVendidasEnEstePedido);

        if (activa.getStockActual() >= activa.getStockMaximo()) {
            activa.setActivo(false);
            preVentaRepository.save(activa);

            List<Pedido> anulados = pedidoRepository.findByPreVentaIdAndAnuladoTrue(activa.getId());
            pedidoRepository.deleteAll(anulados);
        } else {
            preVentaRepository.save(activa);
        }

        for (PedidoDetalle detalle : pedido.getDetalles()) {
            if (Boolean.TRUE.equals(detalle.getEsPack()) && detalle.getPack() != null) {
                for (Cookie c : detalle.getPack().getGalletas()) {
                    c.setStockActual(c.getStockActual() - detalle.getCantidad());
                    cookieRepository.save(c);
                }
            } else if (detalle.getCookie() != null) {
                Cookie c = detalle.getCookie();
                c.setStockActual(c.getStockActual() - detalle.getCantidad());
                cookieRepository.save(c);
            }
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

        boolean nuevoEstadoAnulado = !(pedido.getAnulado() != null && pedido.getAnulado());
        pedido.setAnulado(nuevoEstadoAnulado);

        int impactoEnCookies = pedido.getDetalles().stream()
                .mapToInt(d -> Boolean.TRUE.equals(d.getEsPack()) ? d.getCantidad() * 4 : d.getCantidad())
                .sum();

        for (PedidoDetalle detalle : pedido.getDetalles()) {
            int factor = nuevoEstadoAnulado ? 1 : -1;

            if (Boolean.TRUE.equals(detalle.getEsPack()) && detalle.getPack() != null) {
                for (Cookie c : detalle.getPack().getGalletas()) {
                    int stockActual = (c.getStockActual() != null) ? c.getStockActual() : 0;
                    c.setStockActual(stockActual + (detalle.getCantidad() * factor));
                    cookieRepository.save(c);
                }
            } else if (detalle.getCookie() != null) {
                Cookie cookie = detalle.getCookie();
                int stockActual = (cookie.getStockActual() != null) ? cookie.getStockActual() : 0;
                cookie.setStockActual(stockActual + (detalle.getCantidad() * factor));
                cookieRepository.save(cookie);
            }
        }

        PreVenta pv = pedido.getPreVenta();
        if (pv != null) {
            int ajustePreVenta = nuevoEstadoAnulado ? (impactoEnCookies * -1) : impactoEnCookies;
            pv.setStockActual(pv.getStockActual() + ajustePreVenta);

            if (pv.getStockActual() >= pv.getStockMaximo()) {
                pv.setActivo(false);
            }
            preVentaRepository.save(pv);
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