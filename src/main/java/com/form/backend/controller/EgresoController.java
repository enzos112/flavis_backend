package com.form.backend.controller;

import com.form.backend.dto.EgresoRequestDTO;
import com.form.backend.model.Egreso;
import com.form.backend.service.EgresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/egresos")
public class EgresoController {

    @Autowired
    private EgresoService egresoService;

    @PostMapping
    public ResponseEntity<?> registrarEgreso(@RequestBody EgresoRequestDTO dto) {
        try {
            egresoService.registrarEgreso(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Egreso registrado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody EgresoRequestDTO dto) {
        try {
            return ResponseEntity.ok(egresoService.actualizarEgreso(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            egresoService.eliminarEgreso(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/preventa/{id}")
    public ResponseEntity<List<Egreso>> obtenerPorPreVenta(@PathVariable Long id) {
        List<Egreso> egresos = egresoService.obtenerEgresosPorCampaña(id);
        return ResponseEntity.ok(egresos);
    }

    @GetMapping
    public ResponseEntity<List<Egreso>> listarTodos() {
        return ResponseEntity.ok(egresoService.listarTodos());
    }
}