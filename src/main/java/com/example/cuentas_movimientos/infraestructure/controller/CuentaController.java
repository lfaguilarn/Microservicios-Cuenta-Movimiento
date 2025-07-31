package com.example.cuentas_movimientos.infraestructure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cuentas_movimientos.domain.model.Cuenta;
import com.example.cuentas_movimientos.service.CuentaService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/cuentas")
@CrossOrigin(origins = "*")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public List<Cuenta> list() {
        return cuentaService.listAll();
    }

    @GetMapping("/{id}")
    public Cuenta get(@PathVariable Long id) {
        return cuentaService.getById(id);
    }

    @PostMapping
    public Cuenta create(@Valid @RequestBody Cuenta cuenta) {
        return cuentaService.save(cuenta);
    }

    @PutMapping("/{id}")
    public Cuenta update(@PathVariable Long id, @Valid @RequestBody Cuenta cuenta) {
        return cuentaService.update(id, cuenta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
