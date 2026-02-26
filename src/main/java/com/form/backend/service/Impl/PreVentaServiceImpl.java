package com.form.backend.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.form.backend.model.Cookie;
import com.form.backend.model.Pedido;
import com.form.backend.model.PreVenta;
import com.form.backend.repositorio.CookieRepository;
import com.form.backend.repositorio.PreVentaRepository;
import com.form.backend.repositorio.PedidoRepository;
import com.form.backend.service.PreVentaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PreVentaServiceImpl implements PreVentaService {

    @Autowired
    private PreVentaRepository preVentaRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private CookieRepository cookieRepository;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public PreVenta crear(PreVenta preVenta) {
        return preVentaRepository.save(preVenta);
    }

    @Override
    public Optional<PreVenta> obtenerActiva() {
        return preVentaRepository.findByActivoTrueOrderByIdDesc().stream().findFirst().map(pv -> {
            Long vendidos = pedidoRepository.countTotalCookiesByPreVentaId(pv.getId());
            long totalVendidos = (vendidos != null ? vendidos : 0L);
            pv.setStockActual(totalVendidos);

            if (totalVendidos >= pv.getStockMaximo()) {
                cerrarPreVenta(pv.getId());
                return null;
            }

            return pv;
        });
    }

    @Override
    public PreVenta actualizar(Long id, PreVenta nuevosDatos) {
        return preVentaRepository.findById(id).map(pv -> {
            pv.setNombreCampania(nuevosDatos.getNombreCampania());
            pv.setFechaApertura(nuevosDatos.getFechaApertura());
            pv.setFechaCierre(nuevosDatos.getFechaCierre());
            pv.setFechaEntrega(nuevosDatos.getFechaEntrega());

            pv.setHorarioEntrega(nuevosDatos.getHorarioEntrega());
            pv.setHorarioDelivery(nuevosDatos.getHorarioDelivery());
            pv.setHorarioRecojo(nuevosDatos.getHorarioRecojo());

            pv.setMensajeCierre(nuevosDatos.getMensajeCierre());
            pv.setQrUrl(nuevosDatos.getQrUrl());
            pv.setStockMaximo(nuevosDatos.getStockMaximo());

            Long vendidos = pedidoRepository.countTotalCookiesByPreVentaId(id);
            long totalVendidos = (vendidos != null ? vendidos : 0L);

            if (totalVendidos >= nuevosDatos.getStockMaximo()) {
                pv.setActivo(false);
            } else {
                pv.setActivo(nuevosDatos.getActivo());
            }

            return preVentaRepository.save(pv);
        }).orElseThrow(() -> new RuntimeException("Error ID: " + id));
    }

    @Override
    public List<PreVenta> listarHistorial() { return preVentaRepository.findAll(); }

    @Override
    @Transactional
    public void cerrarPreVenta(Long id) {
        preVentaRepository.findById(id).ifPresent(pv -> {
            if (!pv.getActivo()) return;

            List<Pedido> anulados = pedidoRepository.findByPreVentaIdAndAnuladoTrue(id);

            for (Pedido p : anulados) {
                String publicId = extraerPublicId(p.getComprobanteUrl());
                if (publicId != null) {
                    try {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    } catch (Exception e) {
                        System.err.println("Error eliminando imagen de pedido anulado: " + e.getMessage());
                    }
                }
                pedidoRepository.delete(p);
            }

            pv.setActivo(false);
            preVentaRepository.save(pv);
        });
    }

    private String extraerPublicId(String url) {
        try {
            if (url == null || !url.contains("/")) return null;
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            return fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
        } catch (Exception e) { return null; }
    }
}