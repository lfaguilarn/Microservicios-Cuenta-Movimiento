package com.example.cuentas_movimientos.infraestructure.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cuentas_movimientos.domain.model.Cuenta;
import com.example.cuentas_movimientos.domain.model.Movimiento;
import com.example.cuentas_movimientos.domain.model.dto.MovimientoResponseDTO;
import com.example.cuentas_movimientos.service.MovimientoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    // ✅ Endpoint para listar todos los movimientos
    @GetMapping
    public ResponseEntity<List<Movimiento>> listar() {
        return ResponseEntity.ok(movimientoService.listarMovimientos());
    }
    
    // ✅ Endpoint para obtener un movimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.findById(id));
    }
    // ✅ Endpoint para obtener un movimiento por fecha y por usuario

    @GetMapping("/reportes")
    public ResponseEntity<List<MovimientoResponseDTO>> buscarMovimientosPorClienteYFechas(
        @RequestParam Long clienteId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<Movimiento> movimientos = movimientoService.findByCuentaClienteIdAndFechaBetween(clienteId, fechaInicio, fechaFin);

        List<MovimientoResponseDTO> respuesta = movimientos.stream().map(movimiento -> {
            Cuenta cuenta = movimiento.getCuenta();

            String nombreCliente = movimientoService.obtenerNombreClientePorId(cuenta.getClienteId());

            MovimientoResponseDTO dto = new MovimientoResponseDTO();
            dto.setFecha(movimiento.getFecha().toLocalDate().toString());
            dto.setCliente(nombreCliente);
            dto.setNumeroCuenta(cuenta.getNumeroCuenta());
            dto.setTipo(cuenta.getTipoCuenta());
            dto.setSaldoInicial(movimiento.getSaldo());
            dto.setEstado(cuenta.getEstado());
            dto.setMovimiento(movimiento.getValor());
            dto.setSaldoDisponible(movimiento.getSaldo()+movimiento.getValor());

            return dto;
        }).toList();

        return ResponseEntity.ok(respuesta);
    }


    // ✅ Endpoint para crear un nuevo movimiento
    @PostMapping
    public ResponseEntity<Movimiento> crear(@RequestBody Movimiento movimiento) {
        Movimiento creado = movimientoService.crearMovimiento(movimiento);
        return ResponseEntity.ok(creado);
    }

    // ✅ Endpoint para eliminar un movimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}
