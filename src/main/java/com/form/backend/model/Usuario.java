package com.form.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Recuerda que esto se debe guardar encriptado (Bcrypt)

    private String rol = "ADMIN"; // Por defecto todos los de esta tabla serán ADMIN
}
