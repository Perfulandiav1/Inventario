package cl.perfulandia.inventario.controller;

import cl.perfulandia.inventario.controlador.InventarioControlador;
import cl.perfulandia.inventario.dto.SucursalDTO;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.modelo.SucursalStock;
import cl.perfulandia.inventario.service.InventarioService;
import cl.perfulandia.inventario.assemblers.AlertaInventarioModelAssembler;
import cl.perfulandia.inventario.assemblers.SucursalStockModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.hateoas.EntityModel;
import java.util.List;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test para el controlador de inventario.
 * Verifica que los endpoints devuelvan las respuestas esperadas.
 */
@WebMvcTest(InventarioControlador.class)
class InventarioControladorTest {
    /**
     * MockMvc para simular peticiones HTTP al controlador.
     */
    @Autowired
    private MockMvc mockMvc;
    
    /**
     * Servicio de inventario simulado.
     * Permite verificar la lógica del controlador sin depender de la implementación real.
     */
    @MockBean
    private InventarioService inventarioService;

    /**
     * Ensamblador de modelos de alertas simulado.
     * Utilizado para convertir entidades a modelos HATEOAS.
     */
    @MockBean
    private AlertaInventarioModelAssembler alertaInventarioModelAssembler;

    /**
     * Ensamblador de modelos de SucursalStock simulado.
     * Utilizado para convertir entidades a modelos HATEOAS.
     */
    @MockBean
    private SucursalStockModelAssembler sucursalStockModelAssembler;

    private SucursalStock stock;
    private AlertaInventario alerta;
    private SucursalDTO sucursalDTO;

    @BeforeEach
    void setUp() {
        stock = new SucursalStock();
        alerta = new AlertaInventario();
        alerta.setId(1L);
        alerta.setSucursalId(2L);
        sucursalDTO = new SucursalDTO();
        sucursalDTO.setSucursalId(2L);
    }
    /**
     * Verifica que el endpoint de obtener stock por sucursal retorne una lista con un stock.
     */
    @Test
    void testObtenerStock() throws Exception {
        when(inventarioService.obtenerStockPorSucursal(1L)).thenReturn(List.of(stock));
        when(sucursalStockModelAssembler.toModel(any(SucursalStock.class)))
                .thenReturn(EntityModel.of(stock));

        mockMvc.perform(get("/api/inventario/stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.sucursalStockList[0].id").doesNotExist()); // Ajusta según tus campos
    }

    //@Test
    //void testListarAlertas() throws Exception {
        //when(inventarioService.listarAlertas()).thenReturn(List.of(alerta));
        //when(alertaInventarioModelAssembler.toModel(any(AlertaInventario.class)))
                //.thenReturn(EntityModel.of(alerta));

        //mockMvc.perform(get("/api/inventario/alertas"))
                //.andExpect(status().isOk())
               // .andExpect(jsonPath("_embedded.alertaInventarioList[0].id").value(alerta.getId()));//
    //}

    /**
     * Verifica que el endpoint de obtener alertas por sucursal retorne una lista con una alerta.
     */
    @Test
    void testObtenerSucursales() throws Exception {
        when(inventarioService.obtenerSucursales()).thenReturn(List.of(sucursalDTO));

        mockMvc.perform(get("/api/inventario/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sucursalId").value(sucursalDTO.getSucursalId()));
    }

    /**
     * Verifica que el endpoint de obtener una sucursal por ID retorne la sucursal esperada.
     */
    @Test
    void testObtenerSucursal() throws Exception {
        when(inventarioService.obtenerSucursalDetalle(2L)).thenReturn(sucursalDTO);

        mockMvc.perform(get("/api/inventario/sucursal/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucursalId").value(sucursalDTO.getSucursalId()));
    }

    //@Test
   // void testObtenerStockVacio() throws Exception {
     //   when(inventarioService.obtenerStockPorSucursal(1L)).thenReturn(Collections.emptyList());

       // mockMvc.perform(get("/api/inventario/stock/1"))
         //       .andExpect(status().isOk())
     //           .andExpect(jsonPath("$.content").isEmpty());
    //}

   // @Test
    //void testListarAlertasVacio() throws Exception {
      //  when(inventarioService.listarAlertas()).thenReturn(Collections.emptyList());

        //mockMvc.perform(get("/api/inventario/alertas"))
          //      .andExpect(status().isOk())
            //    .andExpect(jsonPath("_embedded").exists());
    //}

    /**
     * Verifica que el endpoint de obtener sucursales retorne una lista vacía cuando no hay sucursales.
     */
    @Test
    void testObtenerSucursalesVacio() throws Exception {
        when(inventarioService.obtenerSucursales()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/inventario/sucursales"))
                .andExpect(status().isNoContent());
    }
    /**
     * Verifica que el endpoint de obtener una sucursal por ID retorne un estado 200 OK con una colección vacía
     * cuando la sucursal no existe.
     */
    @Test
    void testObtenerSucursalNoExiste() throws Exception {
        when(inventarioService.obtenerSucursalDetalle(99L)).thenReturn(null);

        mockMvc.perform(get("/api/inventario/sucursal/99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
    /**
     * Verifica que el endpoint de listar alertas retorne una lista con una alerta.
     */
    @Test
    void listarAlertas_vacio_debeRetornarColeccionVacia() throws Exception {
        when(inventarioService.listarAlertas()).thenReturn(List.of());

        mockMvc.perform(get("/api/inventario/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded").doesNotExist());
    }
    /**
     * Verifica que el endpoint de obtener una sucursal por ID lance una excepción si el ID es nulo.
     */
    @Test
    void obtenerSucursal_idNull_lanzaExcepcion() {
        InventarioControlador controlador = new InventarioControlador(inventarioService, sucursalStockModelAssembler);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> controlador.obtenerSucursal(null));
        assertEquals("El ID de sucursal no puede ser nulo", ex.getMessage());
    }
}