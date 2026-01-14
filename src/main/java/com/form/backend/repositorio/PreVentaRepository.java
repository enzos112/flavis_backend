package com.form.backend.repositorio;

import com.form.backend.model.PreVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PreVentaRepository extends JpaRepository<PreVenta, Long> {
    @Query("SELECT p FROM PreVenta p WHERE :now BETWEEN p.fechaApertura AND p.fechaCierre AND p.activo = true ORDER BY p.id DESC")
    List<PreVenta> findActivePreVentas(@Param("now") LocalDateTime now);
    List<PreVenta> findByActivoTrueOrderByIdDesc();
}
