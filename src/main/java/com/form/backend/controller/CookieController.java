package com.form.backend.controller;

import com.form.backend.model.Cookie;
import com.form.backend.service.CookieService;
import com.form.backend.service.CloudinaryService; // Asegúrate de importar tu interfaz
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cookies")
@CrossOrigin(origins = "*")
public class CookieController {

    @Autowired
    private CookieService cookieService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<Cookie> guardar(@Valid @RequestBody Cookie cookie) {
        return ResponseEntity.ok(cookieService.guardar(cookie));
    }

    @GetMapping("/activas")
    public List<Cookie> listarActivas() {
        return cookieService.listarActivas();
    }

    @GetMapping
    public List<Cookie> listarTodas() {
        return cookieService.listarTodas();
    }

    @PostMapping("/upload")
    public ResponseEntity<Map> upload(@RequestParam MultipartFile multipartFile) throws IOException {
        Map result = cloudinaryService.upload(multipartFile);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cookieService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // NUEVO ENDPOINT PARA ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Cookie> actualizar(@PathVariable Long id, @Valid @RequestBody Cookie cookie) {
        return ResponseEntity.ok(cookieService.actualizar(id, cookie));
    }
}