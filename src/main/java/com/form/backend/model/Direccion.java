package com.form.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "direcciones")
@Data
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alias; // Casa, Oficina, etc.
    private String distrito;
    private String detalle; // Av/Jr + Número
    private String referencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_celular")
    @JsonIgnore // Evita recursión infinita al serializar
    private Cliente cliente;
}