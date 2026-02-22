package com.form.backend.dto;

import com.form.backend.model.CategoriaEgreso;
import lombok.Data;

@Data
public class EgresoRequestDTO {
    private String descripcion;
    private Double monto;
    private CategoriaEgreso categoria;
    private Long preVentaId;
}