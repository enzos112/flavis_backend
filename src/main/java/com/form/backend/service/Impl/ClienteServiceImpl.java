package com.form.backend.service.Impl;

import com.form.backend.model.Cliente;
import com.form.backend.model.Pedido;
import com.form.backend.repositorio.ClienteRepository;
import com.form.backend.repositorio.PedidoRepository;
import com.form.backend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public Optional<Cliente> obtenerPorCelular(String celular) {
        return clienteRepository.findById(celular);
    }

    @Override
    public Cliente guardarOActualizar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> todosLosClientes = clienteRepository.findAll();

        return todosLosClientes.stream()
                .map(c -> {
                    List<Pedido> pedidosValidos = pedidoRepository.findByCliente(c).stream()
                            .filter(p -> p.getAnulado() != null && !p.getAnulado())
                            .toList();

                    if (pedidosValidos.isEmpty()) return null;

                    c.setTotalPedidos(pedidosValidos.size());

                    pedidosValidos.stream()
                            .map(Pedido::getFechaCreacion)
                            .max(LocalDateTime::compareTo)
                            .ifPresent(c::setFechaUltimaCompra);

                    double sumaTotal = pedidosValidos.stream()
                            .mapToDouble(p -> p.getMontoTotal() != null ? p.getMontoTotal() : 0.0)
                            .sum();

                    c.setTotalGastado(sumaTotal);
                    return c;
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    @Override
    public Cliente actualizarNotas(String celular, String notas) {
        Cliente cliente = clienteRepository.findById(celular)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con celular: " + celular));

        cliente.setNotas(notas);
        return clienteRepository.save(cliente);
    }
}