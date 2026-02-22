package com.form.backend.repositorio;

import com.form.backend.model.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EgresoRepository extends JpaRepository<Egreso, Long> {

    @Query("SELECT e FROM Egreso e WHERE e.preVenta.id = :preVentaId")
    List<Egreso> findByPreVentaId(@Param("preVentaId") Long preVentaId);
}