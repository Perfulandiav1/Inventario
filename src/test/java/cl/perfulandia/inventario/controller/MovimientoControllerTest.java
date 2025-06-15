package cl.perfulandia.inventario.controller;

import cl.perfulandia.inventario.controlador.MovimientoController;
import cl.perfulandia.inventario.dto.MovimientoRequest;
import cl.perfulandia.inventario.modelo.Movimiento;
import cl.perfulandia.inventario.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimientoController.class)
class MovimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimientoService movimientoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Movimiento movimiento;

    @BeforeEach
    void setUp() {
        movimiento = new Movimiento();
        movimiento.setId(1L);
        // Agrega más atributos si es necesario
    }

    @Test
    void registrarMovimiento_debeRetornarMovimiento() throws Exception {
        MovimientoRequest request = new MovimientoRequest();
        request.setSucursalId(1L);
        request.setProductoId(2L);
        request.setCantidad(10);
        request.setTipo("ENTRADA");

        when(movimientoService.registrarMovimiento(anyLong(), anyLong(), anyInt(), anyString()))
                .thenReturn(movimiento);

        mockMvc.perform(post("/api/movimientos/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movimiento.getId()));
    }

    @Test
    void listarMovimientos_debeRetornarLista() throws Exception {
        when(movimientoService.listarMovimientos()).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/movimientos/Listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movimiento.getId()));
    }

    @Test
    void obtenerMovimiento_debeRetornarMovimiento() throws Exception {
        when(movimientoService.obtenerMovimientoPorId(1L)).thenReturn(movimiento);

        mockMvc.perform(get("/api/movimientos/obtener/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movimiento.getId()));
    }

    @Test
    void movimientosPorSucursal_debeRetornarLista() throws Exception {
        when(movimientoService.listarPorSucursal(1L)).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/movimientos/sucursal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movimiento.getId()));
    }

    @Test
    void movimientosPorProducto_debeRetornarLista() throws Exception {
        when(movimientoService.listarPorProducto(2L)).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/movimientos/producto/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movimiento.getId()));
    }

    @Test
    void eliminarMovimiento_debeRetornarNoContent() throws Exception {
        doNothing().when(movimientoService).eliminarMovimiento(1L);

        mockMvc.perform(delete("/api/movimientos/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}