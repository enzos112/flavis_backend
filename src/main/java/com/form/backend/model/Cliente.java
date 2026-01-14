package com.form.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.Transient;

@Entity
@Table(name = "clientes")
@Data
public class Cliente {

    @Id
    @Column(length = 9)
    private String celular;
    private String nombre;
    private String apellido;

    @Column(updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    private String notas;

    @Transient
    private Integer totalPedidos;

    @Transient
    private LocalDateTime fechaUltimaCompra;

    @Transient
    private Double totalGastado = 0.0;
}
