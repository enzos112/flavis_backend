package com.form.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cliente_celular", nullable = false)
    private Cliente cliente;

    @NotBlank(message = "El tipo de entrega es obligatorio")
    private String tipoEntrega;

    private Double costoEnvio = 0.0;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id")
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name = "pre_venta_id", nullable = false)
    private PreVenta preVenta;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.1", message = "El monto debe ser mayor a 0")
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

    @Column(nullable = false)
    private Boolean guardarDatos = false;
}
