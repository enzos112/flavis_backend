package com.form.backend.repositorio;

import com.form.backend.model.Cliente;
import com.form.backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByPreVentaId(Long preVentaId);

    @Query("SELECT COALESCE(SUM(d.cantidad), 0) FROM PedidoDetalle d WHERE d.pedido.preVenta.id = :id AND d.pedido.anulado = false")
    Long countTotalCookiesByPreVentaId(@Param("id") Long id);

    List<Pedido> findByCliente(Cliente cliente);

    List<Pedido> findByPreVentaIdAndAnuladoTrue(Long preVentaId);


}