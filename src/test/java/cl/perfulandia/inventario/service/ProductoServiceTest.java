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

    @Mock
    private ProductoRepositorio productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarProductos_debeRetornarLista() {
        List<Producto> productos = List.of(new Producto(), new Producto());
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.listarProductos();

        assertEquals(2, resultado.size());
        verify(productoRepository).findAll();
    }

    @Test
    void obtenerProductoPorId_existente() {
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerProductoPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(productoRepository).findById(1L);
    }

    @Test
    void obtenerProductoPorId_noExistente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> productoService.obtenerProductoPorId(1L));
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void crearProducto_ok() {
        Producto producto = new Producto();
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.crearProducto(producto);

        assertEquals(producto, resultado);
        verify(productoRepository).save(producto);
    }

    @Test
    void actualizarProducto_existente() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Viejo");

        Producto actualizado = new Producto();
        actualizado.setNombre("Nuevo");
        actualizado.setDescripcion("Desc");
        actualizado.setStock(10);
        actualizado.setStockMinimo(2);
        actualizado.setPrecio(100.0);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenReturn(producto);

        Producto resultado = productoService.actualizarProducto(1L, actualizado);

        assertEquals("Nuevo", resultado.getNombre());
        assertEquals("Desc", resultado.getDescripcion());
        assertEquals(10, resultado.getStock());
        assertEquals(2, resultado.getStockMinimo());
        assertEquals(100.0, resultado.getPrecio());
        verify(productoRepository).save(producto);
    }

    @Test
    void actualizarProducto_noExistente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> productoService.actualizarProducto(1L, new Producto()));
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void eliminarProducto_ok() {
        productoService.eliminarProducto(1L);
        verify(productoRepository).deleteById(1L);
    }
}
