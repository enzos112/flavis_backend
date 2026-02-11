package com.form.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@Data
public class Cliente {

    @Id
    @NotBlank(message = "El celular es obligatorio")
    @Size(min = 9, max = 9, message = "El celular debe tener 9 dígitos")
    @Pattern(regexp = "^9\\d{8}$", message = "El celular debe empezar con 9")
    @Column(length = 9)
    private String celular;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Column(updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    private String notas;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();

    @Transient
    private Integer totalPedidos;

    @Transient
    private LocalDateTime fechaUltimaCompra;

    @Transient
    private Double totalGastado = 0.0;

    @Column(nullable = false)
    private Boolean guardarDatos = false;
}
