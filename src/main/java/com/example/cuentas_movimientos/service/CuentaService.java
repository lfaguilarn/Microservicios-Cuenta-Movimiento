package com.example.cuentas_movimientos.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cuentas_movimientos.domain.model.Cuenta;
import com.example.cuentas_movimientos.infraestructure.repository.CuentaRepository;

import java.util.List;

@Service
public class CuentaService {
    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Cuenta> listAll() {
        return cuentaRepository.findAll();
    }

    public Cuenta getById(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public Cuenta update(Long id, Cuenta cuenta) {
        Cuenta c = getById(id);
        cuenta.setCuentaId(c.getCuentaId());
        return cuentaRepository.save(cuenta);
    }

    public void delete(Long id) {
        cuentaRepository.deleteById(id);
    }
}
