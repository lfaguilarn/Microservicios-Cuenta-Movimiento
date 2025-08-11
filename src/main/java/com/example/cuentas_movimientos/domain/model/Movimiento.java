package com.example.cuentas_movimientos.domain.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movimientoId;

    private LocalDateTime fecha;
    // private Double saldoDisponible;

    @NotBlank
    private String tipoMovimiento;

    @NotNull
    private Double valor;

    @NotNull
    private Double saldo;

    @NotNull
    private Double saldoDisponible;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

}
