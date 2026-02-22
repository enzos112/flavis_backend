package com.form.backend.service.Impl;

import com.form.backend.dto.EgresoRequestDTO;
import com.form.backend.model.Egreso;
import com.form.backend.model.PreVenta;
import com.form.backend.repositorio.EgresoRepository;
import com.form.backend.repositorio.PreVentaRepository;
import com.form.backend.service.EgresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EgresoServiceImpl implements EgresoService {

    @Autowired
    private EgresoRepository egresoRepository;
    @Autowired
    private PreVentaRepository preVentaRepository;

    @Override
    @Transactional
    public Egreso registrarEgreso(EgresoRequestDTO dto) {
        PreVenta preVenta = preVentaRepository.findById(dto.getPreVentaId())
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        Egreso egreso = new Egreso();
        egreso.setMonto(dto.getMonto());
        egreso.setDescripcion(dto.getDescripcion());
        egreso.setCategoria(dto.getCategoria());
        egreso.setPreVenta(preVenta);

        return egresoRepository.save(egreso);
    }

    @Override
    @Transactional
    public Egreso actualizarEgreso(Long id, EgresoRequestDTO dto) {
        Egreso egreso = egresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Egreso no encontrado"));

        egreso.setDescripcion(dto.getDescripcion());
        egreso.setMonto(dto.getMonto());
        egreso.setCategoria(dto.getCategoria());

        return egresoRepository.save(egreso);
    }

    @Override
    @Transactional
    public void eliminarEgreso(Long id) {
        Egreso egreso = egresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Egreso no encontrado"));

        egresoRepository.deleteById(id);
    }

    @Override
    public List<Egreso> obtenerEgresosPorCampaña(Long preVentaId) {
        return egresoRepository.findByPreVentaId(preVentaId);
    }

    @Override
    public List<Egreso> listarTodos() {
        return egresoRepository.findAll();
    }
}