package com.form.backend.service.Impl;

import com.form.backend.dto.PackRequestDTO;
import com.form.backend.model.Cookie;
import com.form.backend.model.Pack;
import com.form.backend.repositorio.CookieRepository;
import com.form.backend.repositorio.PackRepository;
import com.form.backend.service.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackServiceImpl implements PackService {

    @Autowired
    private PackRepository packRepository;

    @Autowired
    private CookieRepository cookieRepository;

    @Override
    public List<Pack> listarTodos() {
        return packRepository.findAll();
    }

    @Override
    public Pack guardar(PackRequestDTO dto) {
        if (dto.getGalletasIds() == null || dto.getGalletasIds().isEmpty()) {
            throw new RuntimeException("Un pack debe tener al menos 1 galleta seleccionada");
        }

        Pack pack = new Pack();
        return mapearYGuardar(pack, dto);
    }

    @Override
    public Pack actualizar(Long id, PackRequestDTO dto) {
        Pack pack = packRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pack no encontrado"));

        return mapearYGuardar(pack, dto);
    }

    @Override
    public void eliminar(Long id) {
        packRepository.deleteById(id);
    }

    private Pack mapearYGuardar(Pack pack, PackRequestDTO dto) {
        pack.setNombre(dto.getNombre());
        pack.setDescripcion(dto.getDescripcion());
        pack.setPrecio(dto.getPrecio());
        pack.setImagenUrl(dto.getImagenUrl());
        pack.setActivo(dto.isActivo());
        pack.setCostoProduccion(dto.getCostoProduccion());

        List<Cookie> galletasSeleccionadas = dto.getGalletasIds().stream()
                .map(cookieId -> cookieRepository.findById(cookieId)
                        .orElseThrow(() -> new RuntimeException("Galleta con ID " + cookieId + " no existe")))
                .collect(Collectors.toList());

        pack.setGalletas(galletasSeleccionadas);
        return packRepository.save(pack);
    }
}
