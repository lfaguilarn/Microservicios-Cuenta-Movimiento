package com.example.cuentas_movimientos.infraestructure.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cuentas_movimientos.domain.model.Cuenta;

import java.util.Optional;
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numero);
}
