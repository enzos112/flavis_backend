package com.form.backend.service.Impl;

import com.form.backend.model.Cookie;
import com.form.backend.repositorio.CookieRepository;
import com.form.backend.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CookieServiceImpl implements CookieService {

    @Autowired
    private CookieRepository cookieRepository;

    @Override
    public Cookie guardar(Cookie cookie) {
        return cookieRepository.save(cookie);
    }

    @Override
    public List<Cookie> listarActivas() {
        return cookieRepository.findByActivoTrue();
    }

    @Override
    public List<Cookie> listarTodas() {
        return cookieRepository.findAll();
    }

    @Override
    public void eliminar(Long id) {
        cookieRepository.deleteById(id);
    }

    @Override
    public Cookie actualizar(Long id, Cookie cookieActualizada) {
        return cookieRepository.findById(id).map(cookie -> {
            cookie.setNombre(cookieActualizada.getNombre());
            cookie.setDescripcion(cookieActualizada.getDescripcion());
            cookie.setPrecio(cookieActualizada.getPrecio());
            cookie.setImagenUrl(cookieActualizada.getImagenUrl());
            cookie.setActivo(cookieActualizada.getActivo());
            cookie.setCostoProduccion(cookieActualizada.getCostoProduccion());
            return cookieRepository.save(cookie);
        }).orElseThrow(() -> new RuntimeException("Galleta no encontrada con id: " + id));
    }
}