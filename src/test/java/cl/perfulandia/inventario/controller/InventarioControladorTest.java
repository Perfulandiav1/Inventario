package cl.perfulandia.inventario.controller;

import cl.perfulandia.inventario.controlador.InventarioControlador;
import cl.perfulandia.inventario.dto.SucursalDTO;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.modelo.SucursalStock;
import cl.perfulandia.inventario.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioControlador.class)
class InventarioControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;


    private SucursalStock stock;
    private AlertaInventario alerta;
    private SucursalDTO sucursalDTO;

    @BeforeEach
    void setUp() {
        stock = new SucursalStock();
        alerta = new AlertaInventario();
        alerta.setId(1L);
        sucursalDTO = new SucursalDTO();
        sucursalDTO.setSucursalId(2L);
    }

    @Test
    void testObtenerStock() throws Exception {
        when(inventarioService.obtenerStockPorSucursal(1L)).thenReturn(List.of(stock));

        mockMvc.perform(get("/api/inventario/stock/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarAlertas() throws Exception {
        when(inventarioService.listarAlertas()).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/inventario/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(alerta.getId()));
    }

    @Test
    void testObtenerSucursales() throws Exception {
        when(inventarioService.obtenerSucursales()).thenReturn(List.of(sucursalDTO));

        mockMvc.perform(get("/api/inventario/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sucursalId").value(sucursalDTO.getSucursalId()));
    }

    @Test
    void testObtenerSucursal() throws Exception {
        when(inventarioService.obtenerSucursalDetalle(2L)).thenReturn(sucursalDTO);

        mockMvc.perform(get("/api/inventario/sucursal/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucursalId").value(sucursalDTO.getSucursalId()));
    }
}