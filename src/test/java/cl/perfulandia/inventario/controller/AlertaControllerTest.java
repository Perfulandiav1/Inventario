package cl.perfulandia.inventario.controller;
import cl.perfulandia.inventario.controlador.AlertaController;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.service.AlertaInventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertaController.class)
class AlertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertaInventarioService alertaService;

    private AlertaInventario alerta;

    @BeforeEach
    void setUp() {
        alerta = new AlertaInventario();
        alerta.setId(1L);
    }

    @Test
    void listarTodas_debeRetornarLista() throws Exception {
        when(alertaService.obtenerTodasAlertas()).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/alertas/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(alerta.getId()));
    }

    @Test
    void listarPorSucursal_debeRetornarLista() throws Exception {
        when(alertaService.obtenerAlertasPorSucursal(1L)).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/alertas/obtener/sucursal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(alerta.getId()));
    }

    @Test
    void listarPorProducto_debeRetornarLista() throws Exception {
        when(alertaService.obtenerAlertasPorProducto(2L)).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/alertas/obtener/producto/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(alerta.getId()));
    }

    @Test
    void listarPorSucursalYProducto_debeRetornarLista() throws Exception {
        when(alertaService.obtenerAlertasPorSucursalYProducto(1L, 2L)).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/alertas/obtener/sucursal/1/producto/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(alerta.getId()));
    }
}
