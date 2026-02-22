package com.form.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "egresos")
@Data
@ToString(exclude = "preVenta")
@EqualsAndHashCode(exclude = "preVenta")
public class Egreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private Double monto;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CategoriaEgreso categoria;

    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_venta_id", nullable = false)
    @JsonIgnore
    private PreVenta preVenta;

    @JsonProperty("preVentaId")
    public Long getPreVentaId() {
        return this.preVenta != null ? this.preVenta.getId() : null;
    }
}