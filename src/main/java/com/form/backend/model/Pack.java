package com.form.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "packs")
@Data // <--- Genera Getters, Setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagenUrl;
    private boolean activo = true;

    @ManyToMany
    @JoinTable(
            name = "pack_cookies",
            joinColumns = @JoinColumn(name = "pack_id"),
            inverseJoinColumns = @JoinColumn(name = "cookie_id")
    )
    private List<Cookie> galletas;
}