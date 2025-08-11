package com.example.cuentas_movimientos.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.cuentas_movimientos.SaldoInsuficienteException;
import com.example.cuentas_movimientos.domain.model.Cuenta;
import com.example.cuentas_movimientos.domain.model.Movimiento;
import com.example.cuentas_movimientos.domain.model.dto.ClienteDTO;
import com.example.cuentas_movimientos.infraestructure.repository.CuentaRepository;
import com.example.cuentas_movimientos.infraestructure.repository.MovimientoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Servicio que maneja la lógica de creación y gestión de movimientos.
 */
@Service
@RequiredArgsConstructor
public class MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    /**
     * Crea un nuevo movimiento (depósito o retiro) y actualiza el saldo.
     */
    @Transactional
    public Movimiento crearMovimiento(Movimiento movimiento) {

        Cuenta cuenta = cuentaRepository.findById(movimiento.getCuenta().getCuentaId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        // Establecer la fecha actual para el movimiento
        movimiento.setFecha(LocalDateTime.now());
        // Obtener el saldo actual
        Double saldoActual = cuenta.getSaldoInicial();
        movimiento.setSaldo(saldoActual);
        Double valor = movimiento.getValor();

        switch (movimiento.getTipoMovimiento()) {
            case "DEPOSITO":
                cuenta.setSaldo(saldoActual + valor);
                break;

            case "RETIRO":
                if (valor > saldoActual) {
                    throw new SaldoInsuficienteException("Saldo insuficiente para realizar el retiro");
                }
                cuenta.setSaldo(saldoActual - valor);
                movimiento.setValor(valor*-1);
                break;

            default:
                throw new IllegalArgumentException("Tipo de movimiento no válido: " + movimiento.getTipoMovimiento());
        }

        // Actualizar saldo en la cuenta
        cuentaRepository.save(cuenta);

        // Registrar el nuevo saldo en el movimiento para dejarlo almacenado
        movimiento.setSaldoDisponible(cuenta.getSaldo());

        return movimientoRepository.save(movimiento);
    }
    @Autowired
    private RestTemplate restTemplate;
    public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll();
    }
    public Movimiento findById(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }
    public void eliminarMovimiento(Long id) {
        movimientoRepository.deleteById(id);
    }

    public List<Movimiento> findByCuentaClienteIdAndFechaBetween(Long clienteId, LocalDateTime desde,
            LocalDateTime hasta) {
        return movimientoRepository.findByClienteIdAndFechaBetween(clienteId, desde, hasta);
    }
    

    public String obtenerNombreClientePorId(Long clienteId) {
        String url = "http://localhost:8081/clientes/" + clienteId;
        ClienteDTO cliente = restTemplate.getForObject(url, ClienteDTO.class);
        return cliente != null ? cliente.getNombre() : "Desconocido";
    }
}
