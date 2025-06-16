package cl.perfulandia.inventario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.repositorio.AlertaRepositorio;

@Service
public class AlertaInventarioService {
    private static final Logger logger = LoggerFactory.getLogger(AlertaInventarioService.class);
    @Autowired
    private AlertaRepositorio alertaRepo;

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

    public List<AlertaInventario> obtenerAlertasPorSucursal(Long sucursalId) {
        logger.info("Obteniendo alertas para la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        return alertaRepo.findBySucursalId(sucursalId);
    }

    public List<AlertaInventario> obtenerAlertasPorProducto(Long productoId) {
        logger.info("Obteniendo alertas para el producto con ID: {}", productoId);
        if (productoId == null) {
            logger.error("ID de producto no proporcionado");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }
        return alertaRepo.findByProductoId(productoId);
    }

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
