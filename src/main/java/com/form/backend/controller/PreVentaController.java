package com.form.backend.controller;

import com.form.backend.model.PreVenta;
import com.form.backend.service.PreVentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preventas")
@CrossOrigin(origins = "*")
public class PreVentaController {

    @Autowired
    private PreVentaService preVentaService;

    @PostMapping
    public ResponseEntity<PreVenta> crear(@Valid @RequestBody PreVenta pv) {
        return ResponseEntity.ok(preVentaService.crear(pv));
    }

    @GetMapping("/activa")
    public ResponseEntity<PreVenta> obtenerActiva() {
        return preVentaService.obtenerActiva()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping
    public List<PreVenta> listar() {
        return preVentaService.listarHistorial();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreVenta> actualizar(@PathVariable Long id, @Valid @RequestBody PreVenta pv) {
        return ResponseEntity.ok(preVentaService.actualizar(id, pv));
    }
}