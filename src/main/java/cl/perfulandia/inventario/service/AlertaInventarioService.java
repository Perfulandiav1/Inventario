package cl.perfulandia.inventario.service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.repositorio.AlertaRepositorio;

/**
 * AlertaInventarioService.java
 * Este servicio maneja las operaciones relacionadas con las alertas de inventario.
 * Permite obtener, crear y filtrar alertas por sucursal o producto.
 */
@Service
public class AlertaInventarioService {
    /**
     * Logger para registrar eventos en el servicio de alertas.
     * Utilizado para depuración y seguimiento de operaciones.
     */
    private static final Logger logger = LoggerFactory.getLogger(AlertaInventarioService.class);
    /**
     * Repositorio para acceder a los datos de las alertas de inventario.
     * Proporciona métodos para buscar alertas por sucursal, producto o ambos.
     */
    @Autowired
    private AlertaRepositorio alertaRepo;

    /**
     * Constructor del servicio de alertas.
     * Inyecta el repositorio de alertas para realizar operaciones de acceso a datos.
     */
    public List<AlertaInventario> obtenerTodasAlertas() {
        logger.info("Obteniendo todas las alertas de inventario");
        List<AlertaInventario> alertas = alertaRepo.findAll();
        if (alertas.isEmpty()) {
            logger.warn("No se encontraron alertas de inventario");
            return List.of();
        }
        logger.info("Total de alertas encontradas: {}", alertas.size());
        return alertas;
    }
    /**
     * Obtiene las alertas de inventario filtradas por ID de sucursal.
     * @param sucursalId El ID de la sucursal para filtrar las alertas.
     * @return Una lista de AlertaInventario asociada a la sucursal especificada.
     */
    public List<AlertaInventario> obtenerAlertasPorSucursal(Long sucursalId) {
        logger.info("Obteniendo alertas para la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        return alertaRepo.findBySucursalId(sucursalId);
    }

    /**
     * Obtiene las alertas de inventario filtradas por ID de producto.
     * @param productoId El ID del producto para filtrar las alertas.
     * @return Una lista de AlertaInventario asociada al producto especificado.
     */
    public List<AlertaInventario> obtenerAlertasPorProducto(Long productoId) {
        logger.info("Obteniendo alertas para el producto con ID: {}", productoId);
        if (productoId == null) {
            logger.error("ID de producto no proporcionado");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }
        return alertaRepo.findByProductoId(productoId);
    }

    /**
     * Obtiene las alertas de inventario filtradas por ID de sucursal y ID de producto.
     * @param sucursalId El ID de la sucursal para filtrar las alertas.
     * @param productoId El ID del producto para filtrar las alertas.
     * @return Una lista de AlertaInventario asociada a la sucursal y producto especificados.
     */
    public List<AlertaInventario> obtenerAlertasPorSucursalYProducto(Long sucursalId, Long productoId) {
        logger.info("Obteniendo alertas para la sucursal con ID: {} y producto con ID: {}", sucursalId, productoId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        if (productoId == null) {
            logger.error("ID de producto no proporcionado");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }
        return alertaRepo.findBySucursalIdAndProductoId(sucursalId, productoId);
    }

    /*
     * Crea una nueva alerta de inventario.
     * @param alerta La alerta de inventario a crear.
     */
    public void crearAlerta(AlertaInventario alerta) {
        logger.info("Creando nueva alerta de inventario: {}", alerta);
        if (alerta == null) {
            logger.error("Alerta no puede ser nula");
            throw new IllegalArgumentException("La alerta no puede ser nula");
        }
        alerta.setFechaHora(LocalDateTime.now());
        alertaRepo.save(alerta);
    }
}
