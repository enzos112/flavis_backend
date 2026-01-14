package com.form.backend.service;

import com.form.backend.model.PreVenta;
import java.util.List;
import java.util.Optional;

public interface PreVentaService {
    PreVenta crear(PreVenta preVenta);
    Optional<PreVenta> obtenerActiva();
    List<PreVenta> listarHistorial();
    PreVenta actualizar(Long id, PreVenta pv);

    void cerrarPreVenta(Long id);

}