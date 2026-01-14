package com.form.backend.service;

import com.form.backend.model.Cookie;
import java.util.List;

public interface CookieService {
    Cookie guardar(Cookie cookie);
    List<Cookie> listarActivas();
    List<Cookie> listarTodas();
    void eliminar(Long id);
    Cookie actualizar(Long id, Cookie cookie);
}