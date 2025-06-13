package cl.perfulandia.inventario.service;
import cl.perfulandia.inventario.dto.SucursalDTO;
import cl.perfulandia.inventario.feing.SucursalClient;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.modelo.SucursalStock;
import cl.perfulandia.inventario.repositorio.AlertaRepositorio;
import cl.perfulandia.inventario.repositorio.SucursalStockRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class InventarioServiceTest {

    @Mock
    private SucursalClient sucursalClient;
    @Mock
    private SucursalStockRepositorio sucursalStockRepo;
    @Mock
    private AlertaRepositorio alertaRepo;

    @InjectMocks
    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventarioService = new InventarioService(sucursalStockRepo, alertaRepo);
        inventarioService.setSucursalClient(sucursalClient);
    }

    @Test
    void testObtenerStockPorSucursal() {
        Long sucursalId = 1L;
        List<SucursalStock> expected = Arrays.asList(new SucursalStock(), new SucursalStock());
        when(sucursalStockRepo.findBySucursalId(sucursalId)).thenReturn(expected);

        List<SucursalStock> result = inventarioService.obtenerStockPorSucursal(sucursalId);

        assertEquals(expected, result);
        verify(sucursalStockRepo).findBySucursalId(sucursalId);
    }

    @Test
    void testObtenerSucursalDetalle() {
        Long sucursalId = 2L;
        SucursalDTO dto = new SucursalDTO();
        when(sucursalClient.obtenerSucursalPorId(sucursalId)).thenReturn(dto);

        SucursalDTO result = inventarioService.obtenerSucursalDetalle(sucursalId);

        assertEquals(dto, result);
        verify(sucursalClient).obtenerSucursalPorId(sucursalId);
    }

    @Test
    void testListarAlertas() {
        List<AlertaInventario> alertas = Arrays.asList(new AlertaInventario(), new AlertaInventario());
        when(alertaRepo.findAll()).thenReturn(alertas);

        List<AlertaInventario> result = inventarioService.listarAlertas();

        assertEquals(alertas, result);
        verify(alertaRepo).findAll();
    }

    @Test
    void testObtenerSucursalPorIdSuccess() {
        Long id = 3L;
        SucursalDTO dto = new SucursalDTO();
        when(sucursalClient.obtenerSucursalPorId(id)).thenReturn(dto);

        SucursalDTO result = inventarioService.obtenerSucursalPorId(id);

        assertEquals(dto, result);
        verify(sucursalClient).obtenerSucursalPorId(id);
    }

    @Test
    void testObtenerSucursalPorIdException() {
        Long id = 4L;
        when(sucursalClient.obtenerSucursalPorId(id)).thenThrow(new RuntimeException("error"));

        SucursalDTO result = inventarioService.obtenerSucursalPorId(id);

        assertNull(result);
        verify(sucursalClient).obtenerSucursalPorId(id);
    }

    @Test
    void testObtenerSucursalesSuccess() {
        List<SucursalDTO> sucursales = Arrays.asList(new SucursalDTO(), new SucursalDTO());
        when(sucursalClient.obtenerTodasSucursales()).thenReturn(sucursales);

        List<SucursalDTO> result = inventarioService.obtenerSucursales();

        assertEquals(sucursales, result);
        verify(sucursalClient).obtenerTodasSucursales();
    }

    @Test
    void testObtenerSucursalesException() {
        when(sucursalClient.obtenerTodasSucursales()).thenThrow(new RuntimeException("error"));

        List<SucursalDTO> result = inventarioService.obtenerSucursales();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sucursalClient).obtenerTodasSucursales();
    }
}
