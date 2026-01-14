package com.form.backend.controller;

import com.form.backend.model.Cliente;
import com.form.backend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<Iterable<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Cliente> obtenerPorCelular(@RequestParam("telefono") String celular) {
        return clienteService.obtenerPorCelular(celular)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> registrar(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.guardarOActualizar(cliente));
    }

    @PutMapping("/{celular}/notas")
    public ResponseEntity<Cliente> actualizarNotas(@PathVariable String celular, @RequestBody Map<String, String> body) {
        String nuevasNotas = body.get("notas");
        return ResponseEntity.ok(clienteService.actualizarNotas(celular, nuevasNotas));
    }
}