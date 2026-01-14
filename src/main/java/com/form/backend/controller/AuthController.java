package com.form.backend.controller;

import com.form.backend.model.Usuario;
import com.form.backend.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("pass");

        System.out.println("Intentando login con: " + email);
        System.out.println("Password recibida: " + password);

        return usuarioRepository.findByEmail(email)
                .filter(user -> {
                    System.out.println("Usuario encontrado en BD: " + user.getEmail());
                    System.out.println("Password en BD: " + user.getPassword());
                    return user.getPassword().equals(password);
                })
                .map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> {
                    System.out.println("Login fallido para: " + email);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                });
    }
}