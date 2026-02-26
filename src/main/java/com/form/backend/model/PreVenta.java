package com.form.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pre_ventas")
@Data
public class PreVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la campaña es obligatorio")
    private String nombreCampania;

    @NotNull(message = "La fecha de apertura es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime fechaApertura;

    @NotNull(message = "La fecha de cierre es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime fechaCierre;

    @NotNull(message = "La fecha de entrega es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEntrega;

    private String horarioEntrega;

    private String horarioDelivery;
    private String horarioRecojo;

    private String mensajeCierre = "¡Gracias! Pronto volveremos con más sabores.";

    private String qrUrl;

    private Integer stockMaximo = 100;

    @Transient
    private Long stockActual = 0L;

    private Boolean activo = true;

    @PrePersist
    @PreUpdate
    public void validarDatos() {
        if (this.qrUrl != null && this.qrUrl.contains("demo")) this.qrUrl = null;
    }


    public String getHorarioDelivery() {
        if (this.horarioDelivery != null && !this.horarioDelivery.trim().isEmpty()) {
            return this.horarioDelivery;
        }
        return this.horarioEntrega != null ? this.horarioEntrega : "Por coordinar";
    }

    public String getHorarioRecojo() {
        if (this.horarioRecojo != null && !this.horarioRecojo.trim().isEmpty()) {
            return this.horarioRecojo;
        }
        return this.horarioEntrega != null ? this.horarioEntrega : "Por coordinar";
    }
}