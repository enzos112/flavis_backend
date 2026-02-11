package com.form.backend.controller;

import com.form.backend.dto.PackRequestDTO;
import com.form.backend.model.Pack;
import com.form.backend.service.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packs")
@CrossOrigin(origins = "*")
public class PackController {

    @Autowired
    private PackService packService;

    @GetMapping
    public List<Pack> listar() {
        return packService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Pack> crear(@RequestBody PackRequestDTO dto) {
        return ResponseEntity.ok(packService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pack> editar(@PathVariable Long id, @RequestBody PackRequestDTO dto) {
        return ResponseEntity.ok(packService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        packService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}