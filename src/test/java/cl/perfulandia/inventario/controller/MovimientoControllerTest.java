package cl.perfulandia.inventario.controller;

import cl.perfulandia.inventario.controlador.MovimientoController;
import cl.perfulandia.inventario.modelo.Movimiento;
import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.service.MovimientoService;
import cl.perfulandia.inventario.assemblers.MovimientoModelAssembler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.hateoas.IanaLinkRelations.SELF;

@WebMvcTest(MovimientoController.class)
class MovimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimientoService movimientoService;

    @MockBean
    private MovimientoModelAssembler movimientoModelAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Movimiento movimiento;

    @BeforeEach
    void setUp() {
        movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setSucursalId(1L);
        movimiento.setCantidad(10);
        movimiento.setTipo("ENTRADA");
        // Suponiendo que Movimiento tiene un campo Producto y Producto tiene un id
        Producto producto = new Producto();
        producto.setId(2L);
        movimiento.setProducto(producto);
    }

    @Test
    void registrarMovimiento_debeRetornarMovimiento() throws Exception {
        when(movimientoService.registrarMovimiento(anyLong(), anyLong(), anyInt(), anyString()))
                .thenReturn(movimiento);

        EntityModel<Movimiento> entityModel = EntityModel.of(movimiento);
        entityModel.add(Link.of("http://localhost/api/movimientos/obtener/1").withSelfRel());
        when(movimientoModelAssembler.toModel(any(Movimiento.class)))
                .thenReturn(entityModel);

        String movimientoJson = objectMapper.writeValueAsString(movimiento);

        mockMvc.perform(post("/api/movimientos/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimientoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(movimiento.getId()));
    }

    @Test
    void listarMovimientos_debeRetornarLista() throws Exception {
        when(movimientoService.listarMovimientos()).thenReturn(List.of(movimiento));
        when(movimientoModelAssembler.toModel(any(Movimiento.class)))
                .thenReturn(EntityModel.of(movimiento));

        mockMvc.perform(get("/api/movimientos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.movimientoList[0].id").value(movimiento.getId()));
    }

    @Test
    void obtenerMovimiento_debeRetornarMovimiento() throws Exception {
        when(movimientoService.obtenerMovimientoPorId(1L)).thenReturn(movimiento);
        when(movimientoModelAssembler.toModel(any(Movimiento.class)))
                .thenReturn(EntityModel.of(movimiento));

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
        when(movimientoService.obtenerMovimientoPorId(1L)).thenReturn(movimiento);
        doNothing().when(movimientoService).eliminarMovimiento(1L);

        mockMvc.perform(delete("/api/movimientos/eliminar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void registrarMovimiento_datosInvalidos_debeRetornarBadRequest() throws Exception {
        Movimiento movimiento = new Movimiento();
        // No seteamos sucursalId, producto, cantidad ni tipo (todos nulos o inválidos)

        String movimientoJson = objectMapper.writeValueAsString(movimiento);

        mockMvc.perform(post("/api/movimientos/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimientoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarMovimiento_serviceRetornaNull_debeRetornarBadRequest() throws Exception {
        Movimiento movimiento = new Movimiento();
        movimiento.setSucursalId(1L);
        movimiento.setProducto(new Producto());
        movimiento.setCantidad(1);
        movimiento.setTipo("ENTRADA");

        when(movimientoService.registrarMovimiento(anyLong(), anyLong(), anyInt(), anyString()))
                .thenReturn(null);

        String movimientoJson = objectMapper.writeValueAsString(movimiento);

        mockMvc.perform(post("/api/movimientos/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimientoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarMovimientos_vacio_debeRetornarColeccionVacia() throws Exception {
        when(movimientoService.listarMovimientos()).thenReturn(List.of());

        mockMvc.perform(get("/api/movimientos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded").doesNotExist());
    }

    @Test
    void obtenerMovimiento_noExistente_debeRetornarModeloVacio() throws Exception {
        when(movimientoService.obtenerMovimientoPorId(99L)).thenReturn(null);
        when(movimientoModelAssembler.toModel(null)).thenReturn(EntityModel.of(new Movimiento()));

        mockMvc.perform(get("/api/movimientos/obtener/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").doesNotExist()); // Ajusta según tu serialización
    }

   
}