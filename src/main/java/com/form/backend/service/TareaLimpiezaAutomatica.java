package com.form.backend.service;

import com.form.backend.model.PreVenta;
import com.form.backend.repositorio.PreVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TareaLimpiezaAutomatica {

    @Autowired
    private PreVentaRepository preVentaRepository;

    @Autowired
    private PreVentaService preVentaService;

    @Scheduled(fixedRate = 300000)
    public void verificarCierrePorFecha() {
        LocalDateTime ahora = LocalDateTime.now();
        List<PreVenta> activas = preVentaRepository.findByActivoTrueOrderByIdDesc();

        for (PreVenta pv : activas) {
            if (ahora.isAfter(pv.getFechaCierre())) {
                preVentaService.cerrarPreVenta(pv.getId());
            }
        }
    }
}