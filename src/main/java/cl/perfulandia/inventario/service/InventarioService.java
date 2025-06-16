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

@Service
@RequiredArgsConstructor
public class InventarioService {
    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);
    @Autowired
    private SucursalClient sucursalClient;
    private final SucursalStockRepositorio sucursalStockRepo;
    private final AlertaRepositorio alertaRepo;

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

    public void setSucursalClient(SucursalClient sucursalClient) {
        this.sucursalClient = sucursalClient;
    }

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

    public SucursalDTO obtenerSucursalPorId(Long id) {
        try {
            return getSucursalClient().obtenerSucursalPorId(id);
        } catch (Exception e) {
            logger.error("Error al llamar a Sucursal: {}", e.getMessage());
            return null; // o lanza excepción personalizada
        }
    }

    public List<SucursalDTO> obtenerSucursales() {
        try {
            return getSucursalClient().obtenerTodasSucursales();
        } catch (Exception e) {
            logger.error("Error al llamar a Sucursal: {}", e.getMessage());
            return Collections.emptyList(); // o lanza excepción personalizada
        }
    }
}



