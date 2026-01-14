package com.form.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE) // Agrega esto
    @JoinColumn(name = "cliente_celular", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "pre_venta_id", nullable = false)
    private PreVenta preVenta;

    @NotNull
    private Double montoTotal;

    @NotBlank(message = "El comprobante de pago es obligatorio")
    private String comprobanteUrl;

    private Boolean notificado = false;

    @Column(nullable = false)
    private Boolean anulado = false;

    @Column(updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoDetalle> detalles;

    private boolean visto = false;
}
