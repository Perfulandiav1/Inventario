package cl.perfulandia.inventario.controller;

import cl.perfulandia.inventario.controlador.ProductoController;
import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
    }

    @Test
    void listarProductos_debeRetornarLista() throws Exception {
        when(productoService.listarProductos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(producto.getId()))
                .andExpect(jsonPath("$[0].nombre").value(producto.getNombre()));
    }

    @Test
    void obtenerProductoPorId_debeRetornarProducto() throws Exception {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/obtener/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(producto.getId()))
                .andExpect(jsonPath("$.nombre").value(producto.getNombre()));
    }

    @Test
    void crearProducto_debeRetornarProductoCreado() throws Exception {
        when(productoService.crearProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos/agregar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(producto.getId()))
                .andExpect(jsonPath("$.nombre").value(producto.getNombre()));
    }

    @Test
    void actualizarProducto_debeRetornarProductoActualizado() throws Exception {
        when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(producto.getId()))
                .andExpect(jsonPath("$.nombre").value(producto.getNombre()));
    }

    @Test
    void eliminarProducto_debeRetornarNoContent() throws Exception {
        doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/productos/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}