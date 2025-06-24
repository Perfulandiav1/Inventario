package cl.perfulandia.inventario.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.repositorio.AlertaRepositorio;

/**
 * Test para el servicio de alertas de inventario.
 * Verifica que los métodos del servicio funcionen correctamente.
 */
class AlertaInventarioServiceTest {

    @Mock
    private AlertaRepositorio alertaRepo;

    @InjectMocks
    private AlertaInventarioService alertaInventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Verifica que el servicio pueda obtener todas las alertas de inventario.
     * Se simula una lista de alertas y se verifica que el servicio las retorne correctamente.
     */
    @Test
    void testObtenerTodasAlertas() {
        List<AlertaInventario> mockAlertas = Arrays.asList(new AlertaInventario(), new AlertaInventario());
        when(alertaRepo.findAll()).thenReturn(mockAlertas);

        List<AlertaInventario> result = alertaInventarioService.obtenerTodasAlertas();

        assertEquals(mockAlertas.size(), result.size());
        verify(alertaRepo, times(1)).findAll();
    }
    /**
     * Verifica que el servicio pueda obtener alertas por sucursal.
     * Se simula una lista de alertas para una sucursal específica y se verifica que el servicio las retorne correctamente.
     */
    @Test
    void testObtenerAlertasPorSucursal() {
        Long sucursalId = 1L;
        List<AlertaInventario> mockAlertas = Arrays.asList(new AlertaInventario(), new AlertaInventario());
        when(alertaRepo.findBySucursalId(sucursalId)).thenReturn(mockAlertas);

        List<AlertaInventario> result = alertaInventarioService.obtenerAlertasPorSucursal(sucursalId);

        assertEquals(mockAlertas.size(), result.size());
        verify(alertaRepo, times(1)).findBySucursalId(sucursalId);
    }
    /**
     * Verifica que el servicio pueda obtener alertas por producto.
     * Se simula una lista de alertas para un producto específico y se verifica que el servicio las retorne correctamente.
     */
    @Test
    void testObtenerAlertasPorProducto() {
        Long productoId = 2L;
        List<AlertaInventario> mockAlertas = Arrays.asList(new AlertaInventario(), new AlertaInventario());
        when(alertaRepo.findByProductoId(productoId)).thenReturn(mockAlertas);

        List<AlertaInventario> result = alertaInventarioService.obtenerAlertasPorProducto(productoId);

        assertEquals(mockAlertas.size(), result.size());
        verify(alertaRepo, times(1)).findByProductoId(productoId);
    }
/**
     * Verifica que el servicio pueda obtener alertas por sucursal y producto.
     * Se simula una lista de alertas para una sucursal y producto específicos y se verifica que el servicio las retorne correctamente.
     */
    @Test
    void testObtenerAlertasPorSucursalYProducto() {
        Long sucursalId = 1L;
        Long productoId = 2L;
        List<AlertaInventario> mockAlertas = Arrays.asList(new AlertaInventario());
        when(alertaRepo.findBySucursalIdAndProductoId(sucursalId, productoId)).thenReturn(mockAlertas);

        List<AlertaInventario> result = alertaInventarioService.obtenerAlertasPorSucursalYProducto(sucursalId, productoId);

        assertEquals(mockAlertas.size(), result.size());
        verify(alertaRepo, times(1)).findBySucursalIdAndProductoId(sucursalId, productoId);
    }
    /**
     * Verifica que el servicio pueda crear una alerta de inventario.
     * Se simula la creación de una alerta y se verifica que se guarde correctamente en el repositorio.
     */
    @Test
    void testCrearAlerta() {
        AlertaInventario alerta = new AlertaInventario();
        alertaInventarioService.crearAlerta(alerta);

        assertNotNull(alerta.getFechaHora());
        verify(alertaRepo, times(1)).save(alerta);
    }
    /**
     * Verifica que el servicio pueda eliminar una alerta de inventario.
     * Se simula la eliminación de una alerta y se verifica que se elimine correctamente del repositorio.
     */
    @Test
    void obtenerAlertasPorSucursalYProducto_sucursalIdNull_lanzaExcepcion() {
        AlertaInventarioService service = new AlertaInventarioService(alertaRepo);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.obtenerAlertasPorSucursalYProducto(null, 1L));
        assertEquals("El ID de sucursal no puede ser nulo", ex.getMessage());
    }
    /**
     * Verifica que el servicio pueda eliminar una alerta de inventario.
     * Se simula la eliminación de una alerta y se verifica que se elimine correctamente del repositorio.
     */    
    @Test
    void obtenerAlertasPorSucursalYProducto_productoIdNull_lanzaExcepcion() {
        AlertaInventarioService service = new AlertaInventarioService(alertaRepo);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.obtenerAlertasPorSucursalYProducto(1L, null));
        assertEquals("El ID de producto no puede ser nulo", ex.getMessage());
    }
    /**
     * Verifica que el servicio pueda eliminar una alerta de inventario.
     * Se simula la eliminación de una alerta y se verifica que se elimine correctamente del repositorio.
     */
    @Test
    void obtenerAlertasPorProducto_productoIdNull_lanzaExcepcion() {
        AlertaInventarioService service = new AlertaInventarioService(alertaRepo);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.obtenerAlertasPorProducto(null));
        assertEquals("El ID de producto no puede ser nulo", ex.getMessage());
    }
    /**
     * Verifica que el servicio pueda eliminar una alerta de inventario.
     * Se simula la eliminación de una alerta y se verifica que se elimine correctamente del repositorio.
     */
    @Test
    void crearAlerta_alertaNull_lanzaExcepcion() {
        AlertaInventarioService service = new AlertaInventarioService(alertaRepo);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.crearAlerta(null));
        assertEquals("La alerta no puede ser nula", ex.getMessage());
    }
}
