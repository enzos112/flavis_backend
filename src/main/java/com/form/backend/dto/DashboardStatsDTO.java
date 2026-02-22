package com.form.backend.dto;

import lombok.Data;
import java.util.Map;

@Data
public class DashboardStatsDTO {
    private Double ingresosTotales;
    private Double costosProduccionTotal;
    private Double egresosTotales;
    private Double utilidadNeta;

    private Double promedioMontoPedido;
    private Long totalGalletasVendidas;
    private Long galletasListas;
}