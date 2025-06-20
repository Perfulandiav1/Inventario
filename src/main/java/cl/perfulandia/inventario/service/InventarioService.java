package cl.perfulandia.inventario.service;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.perfulandia.inventario.dto.SucursalDTO;
import cl.perfulandia.inventario.feing.SucursalClient;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.modelo.SucursalStock;
import cl.perfulandia.inventario.repositorio.AlertaRepositorio;
import cl.perfulandia.inventario.repositorio.SucursalStockRepositorio;
import lombok.RequiredArgsConstructor;

/**
 * InventarioService.java
 * Este servicio maneja las operaciones relacionadas con el inventario.
 * Permite obtener stock por sucursal, listar alertas de inventario y obtener detalles de sucursales.
 */
@Service
@RequiredArgsConstructor
public class InventarioService {
    /**
     * Logger para registrar eventos en el servicio de inventario.
     * Utilizado para depuración y seguimiento de operaciones.
     */
    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);
    /**
     * Repositorio para acceder a los datos de stock de sucursales.
     * Proporciona métodos para buscar stocks por ID de sucursal.
     */
    @Autowired
    private SucursalClient sucursalClient;
    private final SucursalStockRepositorio sucursalStockRepo;
    private final AlertaRepositorio alertaRepo;

    /**
     * Constructor del servicio de inventario.
     * Inyecta el repositorio de stock de sucursales y el repositorio de alertas para realizar operaciones de acceso a datos.
     */
    public List<SucursalStock> obtenerStockPorSucursal(Long sucursalId) {
        logger.info("Obteniendo stock para la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        List<SucursalStock> stock = sucursalStockRepo.findBySucursalId(sucursalId);
        if (stock.isEmpty()) {
            logger.warn("No se encontró stock para la sucursal con ID: {}", sucursalId);
            return Collections.emptyList();
        }
        return stock;
    }

    /**
     * Obtiene el cliente de Sucursal configurado para realizar llamadas a la API de Sucursal.
     * @return SucursalClient configurado.
     * @throws IllegalStateException si el cliente no está configurado.
     */
    public SucursalClient getSucursalClient() {
        logger.info("Obteniendo cliente de Sucursal");
        if (sucursalClient == null) {
            logger.error("SucursalClient no está configurado");
            throw new IllegalStateException("SucursalClient no está configurado");
        }
        logger.info("SucursalClient obtenido correctamente");
        // Aquí podrías agregar más lógica si es necesario
        return sucursalClient;  
    }

    /**
     * Establece el cliente de Sucursal para realizar llamadas a la API de Sucursal.
     * @param sucursalClient Cliente de Sucursal a establecer.
     */
    public void setSucursalClient(SucursalClient sucursalClient) {
        this.sucursalClient = sucursalClient;
    }

    /**
     * Obtiene los detalles de una sucursal específica por su ID.
     * @param sucursalId El ID de la sucursal para obtener sus detalles.
     * @return SucursalDTO con los detalles de la sucursal.
     * @throws IllegalArgumentException si el ID de sucursal es nulo.
     */
    public SucursalDTO obtenerSucursalDetalle(Long sucursalId) {
        logger.info("Obteniendo detalles de la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        logger.info("Llamando al cliente de Sucursal para obtener detalles");
        // Aquí se asume que el cliente de Sucursal está configurado y disponible
        return getSucursalClient().obtenerSucursalPorId(sucursalId);
    }

    /**
     * Listar todas las alertas.
     */
    public List<AlertaInventario> listarAlertas() {
        logger.info("Listando todas las alertas de inventario");
        List<AlertaInventario> alertas = alertaRepo.findAll();
        if (alertas.isEmpty()) {
            logger.warn("No se encontraron alertas de inventario");
            return List.of();
        }
        logger.info("Total de alertas encontradas: {}", alertas.size());
        return alertas;
    }

    /**
     * Obtiene una sucursal específica por su ID.
     * @param id El ID de la sucursal a buscar.
     * @return SucursalDTO con los detalles de la sucursal, o null si no se encuentra.
     */
    public SucursalDTO obtenerSucursalPorId(Long id) {
        try {
            return getSucursalClient().obtenerSucursalPorId(id);
        } catch (Exception e) {
            logger.error("Error al llamar a Sucursal: {}", e.getMessage());
            return null; // o lanza excepción personalizada
        }
    }

    /**
     * Obtiene todas las sucursales disponibles.
     * @return Una lista de SucursalDTO que representa todas las sucursales.
     */
    public List<SucursalDTO> obtenerSucursales() {
        try {
            return getSucursalClient().obtenerTodasSucursales();
        } catch (Exception e) {
            logger.error("Error al llamar a Sucursal: {}", e.getMessage());
            return Collections.emptyList(); // o lanza excepción personalizada
        }
    }
}



