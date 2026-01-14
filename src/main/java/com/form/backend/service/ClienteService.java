package com.form.backend.service;

import com.form.backend.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Optional<Cliente> obtenerPorCelular(String celular);
    Cliente guardarOActualizar(Cliente cliente);
    List<Cliente> listarTodos();
    Cliente actualizarNotas(String celular, String notas);
}