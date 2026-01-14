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
    @JsonIgnore // Evita bucles infinitos en el JSON
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "cookie_id")
    private Cookie cookie;

    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    private Double precioUnitario; // Precio al momento de la compra
}
