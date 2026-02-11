package com.form.backend.service;

import com.form.backend.dto.PackRequestDTO;
import com.form.backend.model.Pack;
import java.util.List;

public interface PackService {
    List<Pack> listarTodos();
    Pack guardar(PackRequestDTO dto);
    Pack actualizar(Long id, PackRequestDTO dto);
    void eliminar(Long id);
}