package com.form.backend.controller;

import com.form.backend.model.Pedido;
import com.form.backend.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // <-- Importante para el Map

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Pedido pedido) {
        try {
            return ResponseEntity.ok(pedidoService.registrarPedido(pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/preventa/{id}")
    public ResponseEntity<List<Pedido>> listarPorPreVenta(@PathVariable Long id) {
        List<Pedido> pedidos = pedidoService.listarPorPreVenta(id);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<?> toggleAnulacion(@PathVariable Long id) {
        try {
            pedidoService.toggleAnulacion(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/visto")
    public ResponseEntity<?> marcarComoVisto(@PathVariable Long id) {
        try {
            pedidoService.marcarComoVisto(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/listo")
    public ResponseEntity<?> marcarComoListo(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        try {
            Boolean estado = payload.getOrDefault("estado", true);
            pedidoService.marcarComoListo(id, estado);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar estado: " + e.getMessage());
        }
    }
}