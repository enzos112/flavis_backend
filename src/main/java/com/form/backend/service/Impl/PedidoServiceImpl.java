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
    @Autowired
    private PackRepository packRepository;

    @Override
    @Transactional
    public Pedido registrarPedido(Pedido pedido) {
        PreVenta activa = preVentaRepository.findByActivoTrueOrderByIdDesc()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No hay pre-venta abierta."));

        pedido.setPreVenta(activa);

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

        if ("DELIVERY".equals(pedido.getTipoEntrega()) && pedido.getDireccion() != null) {
            pedido.getDireccion().setId(null);
        } else if ("RECOJO".equals(pedido.getTipoEntrega())) {
            pedido.setDireccion(null);
            pedido.setCostoEnvio(0.0);
        }

        if (pedido.getDetalles() != null) {
            for (PedidoDetalle detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);

                if (Boolean.TRUE.equals(detalle.getEsPack()) && detalle.getPack() != null) {
                    Pack realPack = packRepository.findById(detalle.getPack().getId()).orElse(null);
                    if (realPack != null) {
                        detalle.setCostoUnitario(realPack.getCostoProduccion() != null ? realPack.getCostoProduccion() : 0.0);
                    }
                } else if (detalle.getCookie() != null) {
                    Cookie realCookie = cookieRepository.findById(detalle.getCookie().getId()).orElse(null);
                    if (realCookie != null) {
                        detalle.setCostoUnitario(realCookie.getCostoProduccion() != null ? realCookie.getCostoProduccion() : 0.0);
                    }
                }
            }
        }

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
                Pack realPack = packRepository.findById(detalle.getPack().getId()).orElse(null);
                if (realPack != null) {
                    for (Cookie c : realPack.getGalletas()) {
                        Cookie realCookie = cookieRepository.findById(c.getId()).get();
                        int stockActual = (realCookie.getStockActual() != null) ? realCookie.getStockActual() : 0;
                        realCookie.setStockActual(stockActual - detalle.getCantidad());
                        cookieRepository.save(realCookie);
                    }
                }
            } else if (detalle.getCookie() != null) {
                Cookie realCookie = cookieRepository.findById(detalle.getCookie().getId())
                        .orElseThrow(() -> new RuntimeException("Galleta no encontrada"));
                int stockActual = (realCookie.getStockActual() != null) ? realCookie.getStockActual() : 0;
                realCookie.setStockActual(stockActual - detalle.getCantidad());
                cookieRepository.save(realCookie);
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

    @Override
    @Transactional
    public void marcarComoListo(Long id, Boolean estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        pedido.setListo(estado != null && estado);
        pedidoRepository.save(pedido);
    }
}