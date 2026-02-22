package com.form.backend.controller;

import com.form.backend.dto.DashboardStatsDTO;
import com.form.backend.service.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class EstadisticaController {

    @Autowired
    private EstadisticaService estadisticaService;

    @GetMapping("/dashboard/{preVentaId}")
    public ResponseEntity<DashboardStatsDTO> obtenerResumenDashboard(@PathVariable Long preVentaId) {
        try {
            DashboardStatsDTO stats = estadisticaService.obtenerResumenFinanciero(preVentaId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}