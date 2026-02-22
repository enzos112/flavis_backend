package com.form.backend.service;

import com.form.backend.dto.DashboardStatsDTO;

public interface EstadisticaService {
    DashboardStatsDTO obtenerResumenFinanciero(Long preVentaId);
}