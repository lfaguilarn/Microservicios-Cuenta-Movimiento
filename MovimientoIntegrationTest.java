package com.example.cuentas_movimientos;
import com.example.cuentas_movimientos.domain.model.Cuenta;
import com.example.cuentas_movimientos.domain.model.Movimiento;
import com.example.cuentas_movimientos.infraestructure.repository.CuentaRepository;
import com.example.cuentas_movimientos.infraestructure.repository.MovimientoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.assertj.core.api.Assertions;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovimientoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        movimientoRepository.deleteAll();
        cuentaRepository.deleteAll();
    }

    @Test
    public void testCrearCuentaYMovimiento() {
        // Paso 1: Crear una cuenta inicial con saldo
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123456789");
        cuenta.setTipoCuenta("AHORROS");
        cuenta.setSaldoInicial(new Double("1000.00"));
        cuenta.setEstado(true);
        cuenta.setClienteId(1L);

        cuenta = cuentaRepository.save(cuenta);

        // Paso 2: Crear un movimiento de retiro válido
        Movimiento movimiento = new Movimiento();
        movimiento.getCuenta().setNumeroCuenta("123456789");
        movimiento.setTipoMovimiento("RETIRO");
        movimiento.setValor(new Double("200.00"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Movimiento> request = new HttpEntity<>(movimiento, headers);

        ResponseEntity<Movimiento> response = restTemplate.postForEntity(
                baseUrl + "/movimientos",
                request,
                Movimiento.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Movimiento responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getValor()).isEqualByComparingTo("200.00");
        assertThat(responseBody.getSaldo()).isEqualByComparingTo("800.00");

        // Validar que el saldo de la cuenta también fue actualizado
        Cuenta updatedCuenta = cuentaRepository.findByNumeroCuenta("123456789").orElse(null);
        assertThat(updatedCuenta).isNotNull();
        assertThat(updatedCuenta.getSaldoInicial()).isEqualByComparingTo("800.00");
    }
}

