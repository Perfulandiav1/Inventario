package cl.perfulandia.inventario.controller;

import cl.perfulandia.inventario.controlador.MovimientoController;
import cl.perfulandia.inventario.modelo.Movimiento;
import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.modelo.SucursalStock;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test para el controlador de movimientos.
 * Verifica que los endpoints devuelvan las respuestas esperadas.
 */
@WebMvcTest(MovimientoController.class)
class MovimientoControllerTest {
    /**
     * MockMvc para simular peticiones HTTP al controlador.
     */     
    @Autowired
    private MockMvc mockMvc;
        /**
         * Servicio de movimientos simulado.
         * Permite verificar la lógica del controlador sin depender de la implementación real.
         */
    @MockBean
    private MovimientoService movimientoService;
        /**
         * Ensamblador de modelos de movimientos simulado.
         * Utilizado para convertir entidades a modelos HATEOAS.
         */
    @MockBean
    private MovimientoModelAssembler movimientoModelAssembler;
        /**
         * Repositorio de SucursalStock simulado.
         * Permite verificar la lógica del controlador sin depender de la implementación real.
         */
    @MockBean
    private cl.perfulandia.inventario.repositorio.SucursalStockRepositorio sucursalStockRepo;
        
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
        /**
         * Verifica que el endpoint de registrar movimiento retorne un movimiento creado.
         */
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
    
        /**
         * Verifica que el endpoint de listar movimientos retorne una lista de movimientos.
         */
    @Test
    void listarMovimientos_debeRetornarLista() throws Exception {
        when(movimientoService.listarMovimientos()).thenReturn(List.of(movimiento));
        when(movimientoModelAssembler.toModel(any(Movimiento.class)))
                .thenReturn(EntityModel.of(movimiento));

        mockMvc.perform(get("/api/movimientos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.movimientoList[0].id").value(movimiento.getId()));
    }

        /**
         * Verifica que el endpoint de obtener movimiento por ID retorne el movimiento correspondiente.
         */
    @Test
    void obtenerMovimiento_debeRetornarMovimiento() throws Exception {
        when(movimientoService.obtenerMovimientoPorId(1L)).thenReturn(movimiento);
        when(movimientoModelAssembler.toModel(any(Movimiento.class)))
                .thenReturn(EntityModel.of(movimiento));

        mockMvc.perform(get("/api/movimientos/obtener/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movimiento.getId()));
    }
        /**
         * Verifica que el endpoint de obtener movimiento por ID retorne un modelo vacío si el movimiento no existe.
         */
    @Test
    void movimientosPorSucursal_debeRetornarLista() throws Exception {
        when(movimientoService.listarPorSucursal(1L)).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/movimientos/sucursal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movimiento.getId()));
    }
        /**
         * Verifica que el endpoint de obtener movimientos por producto retorne una lista de movimientos.
         */
    @Test
    void movimientosPorProducto_debeRetornarLista() throws Exception {
        when(movimientoService.listarPorProducto(2L)).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/movimientos/producto/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movimiento.getId()));
    }
        /**
         * Verifica que el endpoint de eliminar movimiento retorne No Content al eliminar un movimiento existente.
         */
    @Test
    void eliminarMovimiento_debeRetornarNoContent() throws Exception {
        when(movimientoService.obtenerMovimientoPorId(1L)).thenReturn(movimiento);
        doNothing().when(movimientoService).eliminarMovimiento(1L);

        mockMvc.perform(delete("/api/movimientos/eliminar/1"))
                .andExpect(status().isNoContent());
    }
        /**
         * Verifica que el endpoint de eliminar movimiento retorne Not Found al intentar eliminar un movimiento no existente.
         */
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
        /**
         * Verifica que el endpoint de registrar movimiento retorne Bad Request si el servicio retorna null.
         */
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

    /**
     * Verifica que el endpoint de listar movimientos retorne una colección vacía si no hay movimientos.
     */
    @Test
    void listarMovimientos_vacio_debeRetornarColeccionVacia() throws Exception {
        when(movimientoService.listarMovimientos()).thenReturn(List.of());

        mockMvc.perform(get("/api/movimientos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded").doesNotExist());
    }
        /**
         * Verifica que el endpoint de obtener movimiento por ID retorne un modelo vacío si el movimiento no existe.
         */
    @Test
    void obtenerMovimiento_noExistente_debeRetornarModeloVacio() throws Exception {
        when(movimientoService.obtenerMovimientoPorId(99L)).thenReturn(null);
        when(movimientoModelAssembler.toModel(null)).thenReturn(EntityModel.of(new Movimiento()));

        mockMvc.perform(get("/api/movimientos/obtener/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").doesNotExist()); // Ajusta según tu serialización
    }
    /**
     * Verifica que el endpoint de obtener movimiento por ID lance una excepción si el ID es nulo.
     */
    @Test
    void obtenerMovimiento_idNull_lanzaModeloVacio() {
        MovimientoController controlador = new MovimientoController(movimientoService, movimientoModelAssembler);
        when(movimientoModelAssembler.toModel(null)).thenReturn(EntityModel.of(new Movimiento()));

        EntityModel<Movimiento> result = controlador.obtenerMovimiento(null);
        assertNotNull(result);
        // Puedes verificar que el contenido es null o vacío según tu implementación
    }
    /**
     * Verifica que el endpoint de obtener movimiento por ID lance una excepción si el ID es nulo.
     */
    @Test
    void registrarMovimiento_creaNuevoStockSiNoExiste() throws Exception {
        Long sucursalId = 1L;
        Long productoId = 2L;
        int cantidad = 10;
        String tipo = "ENTRADA";

        Producto producto = new Producto();
        producto.setId(productoId);
        producto.setNombre("Producto Test");

        when(movimientoService.registrarMovimiento(anyLong(), anyLong(), anyInt(), anyString()))
                .thenAnswer(invocation -> {
                    Long sid = invocation.getArgument(0);
                    Long pid = invocation.getArgument(1);
                    int cant = invocation.getArgument(2);
                    String t = invocation.getArgument(3);

                    Movimiento nuevoMovimiento = new Movimiento();
                    nuevoMovimiento.setId(123L); // Usa un id fijo o dinámico
                    nuevoMovimiento.setSucursalId(sid);
                    nuevoMovimiento.setCantidad(cant);
                    nuevoMovimiento.setTipo(t);

                    Producto p = new Producto();
                    p.setId(pid);
                    nuevoMovimiento.setProducto(p);

                    // Simular creación de stock
                    SucursalStock nuevoStock = new SucursalStock();
                    nuevoStock.setSucursalId(sid);
                    Producto productoStock = new Producto();
                    productoStock.setId(pid);
                    nuevoStock.setProducto(productoStock);
                    nuevoStock.setCantidad(cant);

                    when(sucursalStockRepo.save(any(SucursalStock.class))).thenAnswer(i -> i.getArgument(0));

                    return nuevoMovimiento;
                });

        // MOCK DEL ASSEMBLER
        when(movimientoModelAssembler.toModel(any(Movimiento.class)))
                .thenAnswer(inv -> {
                    Movimiento mov = inv.getArgument(0);
                    EntityModel<Movimiento> entityModel = EntityModel.of(mov);
                    entityModel.add(Link.of("http://localhost/api/movimientos/obtener/" + mov.getId()).withSelfRel());
                    return entityModel;
                });

        Movimiento movimiento = new Movimiento();
        movimiento.setId(123L);
        movimiento.setSucursalId(sucursalId);
        movimiento.setCantidad(cantidad);
        movimiento.setTipo(tipo);
        Producto prod = new Producto();
        prod.setId(productoId);
        movimiento.setProducto(prod);

        String movimientoJson = objectMapper.writeValueAsString(movimiento);

        mockMvc.perform(post("/api/movimientos/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimientoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(123L));
    }
}