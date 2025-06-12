package cl.perfulandia.inventario.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.repositorio.AlertaRepositorio;

class AlertaInventarioServiceTest {

    @Mock
    private AlertaRepositorio alertaRepo;

    @InjectMocks
    private AlertaInventarioService alertaInventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodasAlertas() {
        List<AlertaInventario> mockAlertas = Arrays.asList(new AlertaInventario(), new AlertaInventario());
        when(alertaRepo.findAll()).thenReturn(mockAlertas);

        List<AlertaInventario> result = alertaInventarioService.obtenerTodasAlertas();

        assertEquals(mockAlertas.size(), result.size());
        verify(alertaRepo, times(1)).findAll();
    }
    @Test
    void testObtenerAlertasPorSucursal() {
        Long sucursalId = 1L;
        List<AlertaInventario> mockAlertas = Arrays.asList(new AlertaInventario(), new AlertaInventario());
        when(alertaRepo.findBySucursalId(sucursalId)).thenReturn(mockAlertas);

        List<AlertaInventario> result = alertaInventarioService.obtenerAlertasPorSucursal(sucursalId);

        assertEquals(mockAlertas.size(), result.size());
        verify(alertaRepo, times(1)).findBySucursalId(sucursalId);
    }

    @Test
    void testObtenerAlertasPorProducto() {
        Long productoId = 2L;
        List<AlertaInventario> mockAlertas = Arrays.asList(new AlertaInventario(), new AlertaInventario());
        when(alertaRepo.findByProductoId(productoId)).thenReturn(mockAlertas);

        List<AlertaInventario> result = alertaInventarioService.obtenerAlertasPorProducto(productoId);

        assertEquals(mockAlertas.size(), result.size());
        verify(alertaRepo, times(1)).findByProductoId(productoId);
    }

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

    @Test
    void testCrearAlerta() {
        AlertaInventario alerta = new AlertaInventario();
        alertaInventarioService.crearAlerta(alerta);

        assertNotNull(alerta.getFechaHora());
        verify(alertaRepo, times(1)).save(alerta);
    }
}
