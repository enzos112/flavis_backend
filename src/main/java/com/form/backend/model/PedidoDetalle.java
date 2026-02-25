package com.form.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_detalles")
@Data
@ToString(exclude = "pedido")
@EqualsAndHashCode(exclude = "pedido")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pack_id", nullable = true)
    private Pack pack;

    private Boolean esPack = false;

    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    private Double precioUnitario;

    private Double costoUnitario;
}
