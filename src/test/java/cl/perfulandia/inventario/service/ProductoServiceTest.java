package cl.perfulandia.inventario.service;

import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.repositorio.ProductoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @InjectMocks
    private ProductoService productoService;

    @Mock
    private ProductoRepositorio productoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    

    @Test
    void obtenerProductoPorId_existente() {
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerProductoPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerProductoPorId_noExistente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> productoService.obtenerProductoPorId(1L));
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void crearProducto_debeGuardarYRetornarProducto() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test"); // <-- Agrega esto
        producto.setPrecio(100.0);           // <-- Y esto

        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.crearProducto(producto);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void actualizarProducto_existente() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Original");

        Producto actualizado = new Producto();
        actualizado.setNombre("Nuevo");
        actualizado.setDescripcion("desc");
        actualizado.setStock(10);
        actualizado.setStockMinimo(2);
        actualizado.setPrecio(100.0);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        Producto resultado = productoService.actualizarProducto(1L, actualizado);

        assertEquals("Nuevo", resultado.getNombre());
        assertEquals("desc", resultado.getDescripcion());
        assertEquals(10, resultado.getStock());
        assertEquals(2, resultado.getStockMinimo());
        assertEquals(100.0, resultado.getPrecio());
    }

    @Test
    void actualizarProducto_noExistente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        Producto actualizado = new Producto();
        Exception ex = assertThrows(RuntimeException.class, () -> productoService.actualizarProducto(1L, actualizado));
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void eliminarProducto_debeEliminarPorId() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.eliminarProducto(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void listarProductos_debeRetornarLista() {
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoRepository.count()).thenReturn(1L); // <-- Agrega esto
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.listarProductos();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }
}
