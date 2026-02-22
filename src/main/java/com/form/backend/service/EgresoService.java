package com.form.backend.service;

import com.form.backend.dto.EgresoRequestDTO;
import com.form.backend.model.Egreso;
import java.util.List;

public interface EgresoService {
    Egreso registrarEgreso(EgresoRequestDTO dto);
    Egreso actualizarEgreso(Long id, EgresoRequestDTO dto); // Nuevo
    void eliminarEgreso(Long id); // Nuevo
    List<Egreso> obtenerEgresosPorCampaña(Long preVentaId);
    List<Egreso> listarTodos();
}