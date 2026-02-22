package com.form.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "direcciones")
@Data
@ToString(exclude = "cliente") // <-- CRÍTICO
@EqualsAndHashCode(exclude = "cliente") // <-- CRÍTICO
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alias;
    private String distrito;
    private String detalle;
    private String referencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_celular")
    @JsonIgnore
    private Cliente cliente;
}