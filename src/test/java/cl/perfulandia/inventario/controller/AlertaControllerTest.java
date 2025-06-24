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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import cl.perfulandia.inventario.assemblers.AlertaInventarioModelAssembler;
import org.springframework.hateoas.EntityModel;

/**
 * Test para el controlador de alertas de inventario.
 * Verifica que los endpoints devuelvan las respuestas esperadas.
 */
@WebMvcTest(AlertaController.class)
class AlertaControllerTest {
    /**
     * MockMvc para simular peticiones HTTP al controlador.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Servicio de alertas de inventario simulado.
     * Permite verificar la lógica del controlador sin depender de la implementación real.
     */
    @MockBean
    private AlertaInventarioService alertaService;

    /**
     * Ensamblador de modelos de alertas simulado.
     * Utilizado para convertir entidades a modelos HATEOAS.
     */
    @MockBean
    private AlertaInventarioModelAssembler alertaModelAssembler;

    /**
     * Alerta de inventario de prueba.
     * Se utiliza para simular respuestas del servicio y verificar el comportamiento del controlador.
     */
    private AlertaInventario alerta;

    @BeforeEach
    void setUp() {
        alerta = new AlertaInventario();
        alerta.setId(1L);
    }
    /**
     * Verifica que el endpoint de listar todas las alertas retorne una lista con una alerta.
     */
    @Test
    void listarTodas_debeRetornarLista() throws Exception {
        when(alertaService.obtenerTodasAlertas()).thenReturn(List.of(alerta));
        when(alertaModelAssembler.toModel(any(AlertaInventario.class)))
                .thenReturn(EntityModel.of(alerta));

        mockMvc.perform(get("/api/alertas/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.alertaInventarioList[0].id").value(alerta.getId()));
    }
    /**
     * Verifica que el endpoint de listar alertas por sucursal retorne una lista con una alerta.
     */
    @Test
    void listarPorProducto_debeRetornarLista() throws Exception {
        when(alertaService.obtenerAlertasPorProducto(2L)).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/alertas/obtener/producto/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(alerta.getId()));
    }
    /**
     * Verifica que el endpoint de listar alertas por sucursal y producto retorne una lista con una alerta.
     */
    @Test
    void listarPorSucursalYProducto_debeRetornarLista() throws Exception {
        when(alertaService.obtenerAlertasPorSucursalYProducto(1L, 2L)).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/alertas/obtener/sucursal/1/producto/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(alerta.getId()));
    }
    /**
     * Verifica que el endpoint de listar todas las alertas retorne una lista vacía cuando no hay alertas.
     */
    @Test
    void listarTodas_debeRetornarListaVacia() throws Exception {
        when(alertaService.obtenerTodasAlertas()).thenReturn(List.of());
        when(alertaModelAssembler.toModel(any())).thenReturn(null);

        mockMvc.perform(get("/api/alertas/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.alertaInventarioList").doesNotExist());
    }
    /**
     * Verifica que el endpoint de listar alertas por producto retorne una lista vacía cuando no hay alertas.
     */
    @Test
    void listarPorProducto_sinResultados() throws Exception {
        when(alertaService.obtenerAlertasPorProducto(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/alertas/obtener/producto/99"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    /**
     * Verifica que el endpoint de listar alertas por sucursal y producto retorne una lista vacía cuando no hay alertas.
     */
    @Test
    void listarPorSucursalYProducto_sinResultados() throws Exception {
        when(alertaService.obtenerAlertasPorSucursalYProducto(99L, 88L)).thenReturn(List.of());

        mockMvc.perform(get("/api/alertas/obtener/sucursal/99/producto/88"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    /**
     * Verifica que el endpoint de obtener alerta por ID de sucursal retorne una alerta específica.
     */ 
    @Test
    void obtenerPorId_idNull_lanzaExcepcion() {
        AlertaController controlador = new AlertaController(alertaService, alertaModelAssembler);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> controlador.obtenerPorId(null));
        assertEquals("El ID de sucursal no puede ser nulo", ex.getMessage());
    }
    /**
     * Verifica que el endpoint de obtener alerta por ID de sucursal retorne un modelo de alerta.
     * Si la alerta existe, se debe devolver el modelo correspondiente.
     */
    @Test
    void obtenerPorId_alertaExistente_devuelveModelo() {
        Long sucursalId = 1L;
        AlertaInventario alerta = new AlertaInventario();
        when(alertaService.obtenerAlertasPorSucursal(sucursalId)).thenReturn(List.of(alerta));
        when(alertaModelAssembler.toModel(alerta)).thenReturn(EntityModel.of(alerta));

        AlertaController controlador = new AlertaController(alertaService, alertaModelAssembler);
        EntityModel<AlertaInventario> result = controlador.obtenerPorId(sucursalId);

        assertNotNull(result);
        assertEquals(alerta, result.getContent());
    }
    /**
     * Verifica que el endpoint de obtener alerta por ID de sucursal lance una excepción si la alerta no existe.
     * Si no se encuentra la alerta, se debe lanzar una excepción con un mensaje específico.
     */
    @Test
    void obtenerPorId_alertaNoExistente_lanzaExcepcion() {
        Long sucursalId = 99L;
        when(alertaService.obtenerAlertasPorSucursal(sucursalId)).thenReturn(List.of());

        AlertaController controlador = new AlertaController(alertaService, alertaModelAssembler);
        Exception ex = assertThrows(RuntimeException.class, () -> controlador.obtenerPorId(sucursalId));
        assertTrue(ex.getMessage().contains("Alerta no encontrada para la sucursal con ID: " + sucursalId));
    }
}
