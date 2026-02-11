package com.form.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_detalles")
@Data
public class PedidoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "cookie_id", nullable = true)
    private Cookie cookie;

    @ManyToOne
    @JoinColumn(name = "pack_id", nullable = true)
    private Pack pack;

    private Boolean esPack = false;

    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    private Double precioUnitario;
}
