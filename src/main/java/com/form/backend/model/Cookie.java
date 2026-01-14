package com.form.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "cookies")
@Data
public class Cookie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la galleta es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "1.0", message = "El precio mínimo es S/ 1.00")
    private Double precio;

    @NotBlank(message = "La imagen de Cloudinary es obligatoria")
    private String imagenUrl;

    private Boolean activo = true;

    @NotNull
    private Integer stockActual;
}
