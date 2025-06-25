package cl.perfulandia.inventario.controller;

import cl.perfulandia.inventario.controlador.ProductoController;
import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.service.ProductoService;
import cl.perfulandia.inventario.assemblers.ProductoModelAssembler;
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

/**
 * Test para el controlador de productos.
 * Verifica que los endpoints devuelvan las respuestas esperadas.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {
        /**
         * MockMvc para simular peticiones HTTP al controlador.
         */
    @Autowired
    private MockMvc mockMvc;
        /**
         * Servicio de productos simulado.
         * Permite verificar la lógica del controlador sin depender de la implementación real.
         */
    @MockBean
    private ProductoService productoService;

    @MockBean
    private ProductoModelAssembler productoModelAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setPrecio(1000.0);
    }

        /**
         * Verifica que el endpoint de listar todos los productos retorne una lista con un producto.
         */
    @Test
    void listarProductos_debeRetornarLista() throws Exception {
        when(productoService.listarProductos()).thenReturn(List.of(producto));
        when(productoModelAssembler.toModel(any(Producto.class)))
                .thenReturn(EntityModel.of(producto));

        mockMvc.perform(get("/api/productos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.productoList[0].id").value(producto.getId()))
                .andExpect(jsonPath("_embedded.productoList[0].nombre").value(producto.getNombre()));
    }
        /**
         * Verifica que el endpoint de obtener producto por ID retorne el producto correcto.
         */
    @Test
    void obtenerProductoPorId_debeRetornarProducto() throws Exception {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(producto);
        when(productoModelAssembler.toModel(any(Producto.class)))
                .thenReturn(EntityModel.of(producto));

        mockMvc.perform(get("/api/productos/obtener/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(producto.getId()))
                .andExpect(jsonPath("$.nombre").value(producto.getNombre()));
    }
        /**
         * Verifica que el endpoint de obtener producto por ID retorne un error 404 si el producto no existe.
         */
    @Test
    void crearProducto_debeRetornarProductoCreado() throws Exception {
        when(productoService.crearProducto(any(Producto.class))).thenReturn(producto);

        EntityModel<Producto> entityModel = EntityModel.of(producto);
        entityModel.add(Link.of("http://localhost/api/productos/obtener/1").withSelfRel());
        when(productoModelAssembler.toModel(any(Producto.class)))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/productos/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(producto.getId()))
                .andExpect(jsonPath("$.nombre").value(producto.getNombre()));
    }

    /**
     * Verifica que el endpoint de actualizar producto retorne el producto actualizado.
     * Utiliza un mock del servicio para simular la actualización.
     */
    @Test
    void actualizarProducto_debeRetornarProductoActualizado() throws Exception {
        when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(producto);
        when(productoModelAssembler.toModel(any(Producto.class)))
                .thenReturn(EntityModel.of(producto));

        mockMvc.perform(put("/api/productos/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(producto.getId()))
                .andExpect(jsonPath("$.nombre").value(producto.getNombre()));
    }
        /**
         * Verifica que el endpoint de eliminar producto retorne un No Content (204) al eliminar un producto.
         * Utiliza un mock del servicio para simular la eliminación.
         */
    @Test
    void eliminarProducto_debeRetornarNoContent() throws Exception {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(producto);
        doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/productos/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}