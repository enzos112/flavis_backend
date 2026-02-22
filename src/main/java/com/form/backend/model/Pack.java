package com.form.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "packs")
@Data
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
    private Double costoProduccion = 0.0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "pack_cookies",
            joinColumns = @JoinColumn(name = "pack_id"),
            inverseJoinColumns = @JoinColumn(name = "cookie_id")
    )
    private List<Cookie> galletas;
}