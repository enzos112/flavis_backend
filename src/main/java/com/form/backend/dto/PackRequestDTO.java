package com.form.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class PackRequestDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagenUrl;
    private boolean activo;
    private List<Long> galletasIds;
    private Double costoProduccion;
}