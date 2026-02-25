package com.form.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CookieRequestDTO {

    @NotBlank(message = "El nombre de la galleta es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "1.0", message = "El precio mínimo es S/ 1.00")
    private Double precio;

    @NotBlank(message = "La imagen de Cloudinary es obligatoria")
    private String imagenUrl;

    private Boolean activo;

    private Integer stockActual;

    @NotNull(message = "El costo de producción es obligatorio")
    @DecimalMin(value = "0.0", message = "El costo no puede ser negativo")
    private Double costoProduccion;
}