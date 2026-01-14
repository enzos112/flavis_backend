package com.form.backend.repositorio;

import com.form.backend.model.Cookie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CookieRepository extends JpaRepository<Cookie, Long> {
    List<Cookie> findByActivoTrue(); // Solo las que mi prima quiere vender esta semana
}
