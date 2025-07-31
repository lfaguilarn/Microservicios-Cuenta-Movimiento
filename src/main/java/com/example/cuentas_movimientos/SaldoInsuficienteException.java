package com.example.cuentas_movimientos;

/**
 * Excepción personalizada que se lanza cuando el saldo de una cuenta
 * no es suficiente para realizar un retiro.
 */
public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}