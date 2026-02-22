package com.form.backend.service.Impl;

import com.form.backend.dto.DashboardStatsDTO;
import com.form.backend.model.*;
import com.form.backend.repositorio.*;
import com.form.backend.service.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstadisticaServiceImpl implements EstadisticaService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private EgresoRepository egresoRepository;

    @Override
    public DashboardStatsDTO obtenerResumenFinanciero(Long preVentaId) {
        List<Pedido> pedidos = pedidoRepository.findByPreVentaId(preVentaId).stream()
                .filter(p -> p.getAnulado() == null || !p.getAnulado())
                .toList();

        List<Egreso> egresos = egresoRepository.findByPreVentaId(preVentaId);

        DashboardStatsDTO stats = new DashboardStatsDTO();

        double ingresos = pedidos.stream().mapToDouble(Pedido::getMontoTotal).sum();
        stats.setIngresosTotales(ingresos);

        double costosProd = pedidos.stream()
                .flatMap(p -> p.getDetalles().stream())
                .mapToDouble(d -> {
                    double costoUnitario = 0.0;
                    if (Boolean.TRUE.equals(d.getEsPack()) && d.getPack() != null) {
                        costoUnitario = d.getPack().getCostoProduccion() != null ? d.getPack().getCostoProduccion() : 0.0;
                    } else if (d.getCookie() != null) {
                        costoUnitario = d.getCookie().getCostoProduccion() != null ? d.getCookie().getCostoProduccion() : 0.0;
                    }
                    return costoUnitario * d.getCantidad();
                }).sum();
        stats.setCostosProduccionTotal(costosProd);

        double totalEgresos = egresos.stream().mapToDouble(Egreso::getMonto).sum();
        stats.setEgresosTotales(totalEgresos);

        stats.setUtilidadNeta(ingresos - costosProd - totalEgresos);

        stats.setPromedioMontoPedido(pedidos.isEmpty() ? 0.0 : ingresos / pedidos.size());

        long totalGalletas = pedidos.stream()
                .flatMap(p -> p.getDetalles().stream())
                .mapToLong(d -> Boolean.TRUE.equals(d.getEsPack()) ? d.getCantidad() * 4L : d.getCantidad())
                .sum();
        stats.setTotalGalletasVendidas(totalGalletas);

        long listas = pedidos.stream()
                .filter(p -> Boolean.TRUE.equals(p.getListo()))
                .flatMap(p -> p.getDetalles().stream())
                .mapToLong(d -> Boolean.TRUE.equals(d.getEsPack()) ? d.getCantidad() * 4L : d.getCantidad())
                .sum();
        stats.setGalletasListas(listas);

        return stats;
    }
}