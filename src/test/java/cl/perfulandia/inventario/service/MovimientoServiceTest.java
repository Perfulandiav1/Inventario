package cl.perfulandia.inventario.service;

import cl.perfulandia.inventario.modelo.*;
import cl.perfulandia.inventario.repositorio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoServiceTest {

    @InjectMocks
    private MovimientoService movimientoService;

    @Mock
    private ProductoRepositorio productoRepo;
    @Mock
    private SucursalStockRepositorio sucursalStockRepo;
    @Mock
    private MovimientoRepositorio movimientoRepo;
    @Mock
    private AlertaRepositorio alertaRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarMovimiento_entrada_valido() {
        Long sucursalId = 1L, productoId = 2L;
        Producto producto = new Producto();
        producto.setId(productoId);
        producto.setNombre("TestProd");

        SucursalStock stock = new SucursalStock();
        stock.setSucursalId(sucursalId);
        stock.setProducto(producto);
        stock.setStock(3);

        when(productoRepo.findById(productoId)).thenReturn(Optional.of(producto));
        when(sucursalStockRepo.findAll()).thenReturn(List.of(stock));
        when(sucursalStockRepo.save(any())).thenReturn(stock);
        when(movimientoRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Movimiento mov = movimientoService.registrarMovimiento(sucursalId, productoId, 2, "ENTRADA");

        assertEquals("ENTRADA", mov.getTipo());
        assertEquals(2, mov.getCantidad());
        verify(alertaRepo, never()).save(any(AlertaInventario.class)); // stock queda en 5, no debe crear alerta
    }

    @Test
    void registrarMovimiento_salida_valido() {
        Long sucursalId = 1L, productoId = 2L;
        Producto producto = new Producto();
        producto.setId(productoId);
        producto.setNombre("TestProd");

        SucursalStock stock = new SucursalStock();
        stock.setSucursalId(sucursalId);
        stock.setProducto(producto);
        stock.setStock(10);

        when(productoRepo.findById(productoId)).thenReturn(Optional.of(producto));
        when(sucursalStockRepo.findAll()).thenReturn(List.of(stock));
        when(sucursalStockRepo.save(any())).thenReturn(stock);
        when(movimientoRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Movimiento mov = movimientoService.registrarMovimiento(sucursalId, productoId, 3, "SALIDA");

        assertEquals("SALIDA", mov.getTipo());
        assertEquals(3, mov.getCantidad());
        verify(alertaRepo, never()).save(any());
    }

    @Test
    void registrarMovimiento_tipoInvalido() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            movimientoService.registrarMovimiento(1L, 2L, 1, "OTRO")
        );
        assertTrue(ex.getMessage().contains("Tipo de movimiento inválido"));
    }

    @Test
    void registrarMovimiento_productoNoEncontrado() {
        when(productoRepo.findById(anyLong())).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () ->
            movimientoService.registrarMovimiento(1L, 2L, 1, "ENTRADA")
        );
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void registrarMovimiento_stockInsuficiente() {
        Long sucursalId = 1L, productoId = 2L;
        Producto producto = new Producto();
        producto.setId(productoId);

        SucursalStock stock = new SucursalStock();
        stock.setSucursalId(sucursalId);
        stock.setProducto(producto);
        stock.setStock(1);

        when(productoRepo.findById(productoId)).thenReturn(Optional.of(producto));
        when(sucursalStockRepo.findAll()).thenReturn(List.of(stock));

        Exception ex = assertThrows(IllegalStateException.class, () ->
            movimientoService.registrarMovimiento(sucursalId, productoId, 5, "SALIDA")
        );
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }

    @Test
    void registrarMovimiento_creaAlertaPorStockBajo() {
        Long sucursalId = 1L, productoId = 2L;
        Producto producto = new Producto();
        producto.setId(productoId);
        producto.setNombre("TestProd");

        SucursalStock stock = new SucursalStock();
        stock.setSucursalId(sucursalId);
        stock.setProducto(producto);
        stock.setStock(4);

        when(productoRepo.findById(productoId)).thenReturn(Optional.of(producto));
        when(sucursalStockRepo.findAll()).thenReturn(List.of(stock));
        when(sucursalStockRepo.save(any())).thenReturn(stock);
        when(movimientoRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        movimientoService.registrarMovimiento(sucursalId, productoId, 1, "SALIDA");

        verify(alertaRepo, times(1)).save(any(AlertaInventario.class));
    }

    @Test
    void listarMovimientos_debeRetornarLista() {
        Movimiento mov = new Movimiento();
        mov.setId(1L);
        when(movimientoRepo.findAll()).thenReturn(List.of(mov));

        List<Movimiento> resultado = movimientoService.listarMovimientos();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    void obtenerMovimientoPorId_existente() {
        Movimiento mov = new Movimiento();
        mov.setId(1L);
        when(movimientoRepo.existsById(1L)).thenReturn(true); // <-- Agrega esto
        when(movimientoRepo.findById(1L)).thenReturn(Optional.of(mov));

        Movimiento resultado = movimientoService.obtenerMovimientoPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerMovimientoPorId_noExistente() {
        when(movimientoRepo.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> movimientoService.obtenerMovimientoPorId(1L));
        assertTrue(ex.getMessage().contains("Movimiento no encontrado"));
    }

    @Test
    void listarPorSucursal_debeRetornarLista() {
        Movimiento mov = new Movimiento();
        mov.setId(1L);
        when(movimientoRepo.findBySucursalId(1L)).thenReturn(List.of(mov));

        List<Movimiento> resultado = movimientoService.listarPorSucursal(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    void listarPorProducto_debeRetornarLista() {
        Movimiento mov = new Movimiento();
        mov.setId(1L);
        when(movimientoRepo.findByProductoId(2L)).thenReturn(List.of(mov));

        List<Movimiento> resultado = movimientoService.listarPorProducto(2L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    void eliminarMovimiento_existente() {
        when(movimientoRepo.existsById(1L)).thenReturn(true);
        doNothing().when(movimientoRepo).deleteById(1L);

        movimientoService.eliminarMovimiento(1L);

        verify(movimientoRepo, times(1)).deleteById(1L);
    }

    @Test
    void eliminarMovimiento_noExistente() {
        when(movimientoRepo.existsById(1L)).thenReturn(false);

        Exception ex = assertThrows(RuntimeException.class, () -> movimientoService.eliminarMovimiento(1L));
        assertTrue(ex.getMessage().contains("Movimiento no encontrado"));
    }
}
