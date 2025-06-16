package cl.perfulandia.inventario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.modelo.Movimiento;
import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.modelo.SucursalStock;
import cl.perfulandia.inventario.repositorio.AlertaRepositorio;
import cl.perfulandia.inventario.repositorio.MovimientoRepositorio;
import cl.perfulandia.inventario.repositorio.ProductoRepositorio;
import cl.perfulandia.inventario.repositorio.SucursalStockRepositorio;

@Service
public class MovimientoService {
    private static final Logger logger = LoggerFactory.getLogger(MovimientoService.class);
    @Autowired
    private MovimientoRepositorio movimientoRepo;
    @Autowired
    private SucursalStockRepositorio sucursalStockRepo;
    @Autowired
    private ProductoRepositorio productoRepo;
    @Autowired
    private AlertaRepositorio alertaRepo;

    public Movimiento registrarMovimiento(Long sucursalId, Long productoId, Integer cantidad, String tipo) {
        // Validar tipo de movimiento
        logger.info("Registrando movimiento: Sucursal ID={}, Producto ID={}, Cantidad={}, Tipo={}", sucursalId, productoId, cantidad, tipo);
        if (sucursalId == null || productoId == null || cantidad == null || tipo == null) {
            throw new IllegalArgumentException("Todos los parámetros deben ser proporcionados.");
        }
        if (!tipo.equalsIgnoreCase("ENTRADA") && !tipo.equalsIgnoreCase("SALIDA")) {
            throw new IllegalArgumentException("Tipo de movimiento inválido. Debe ser ENTRADA o SALIDA.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        logger.info("Validación de parámetros completada. Procediendo con el registro del movimiento.");
        // Buscar producto
        logger.info("Buscando producto con ID: {}", productoId);
        Producto producto = productoRepo.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Buscar o crear SucursalStock
        logger.info("Buscando stock para la sucursal con ID: {}", sucursalId);
        SucursalStock stock = sucursalStockRepo.findAll().stream()
            .filter(s -> s.getSucursalId().equals(sucursalId) && s.getProducto().getId().equals(productoId))
            .findFirst()
            .orElseGet(() -> {
                SucursalStock nuevo = new SucursalStock();
                nuevo.setSucursalId(sucursalId);
                nuevo.setProducto(producto);
                nuevo.setStock(0);
                return nuevo;
            });
        logger.info("Stock encontrado: {}", stock);
        // Calcular nuevo stock
        logger.info("Calculando nuevo stock para el producto: {}, Tipo: {}, Cantidad: {}", producto.getNombre(), tipo, cantidad);
         // Verificar si la cantidad es válida para salida
        int nuevoStock = stock.getStock() + (tipo.equalsIgnoreCase("ENTRADA") ? cantidad : -cantidad);
        if (nuevoStock < 0) {
            throw new IllegalStateException("Stock insuficiente para realizar la salida.");
        }
        stock.setStock(nuevoStock);
        sucursalStockRepo.save(stock);

        // Registrar el movimiento
        logger.info("Registrando movimiento de inventario: Producto={}, Sucursal ID={}, Cantidad={}, Tipo={}", producto.getNombre(), sucursalId, cantidad, tipo);
         // Crear y guardar el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setSucursalId(sucursalId);
        movimiento.setCantidad(cantidad);
        movimiento.setTipo(tipo);
        movimiento.setFechaHora(LocalDateTime.now());
        movimientoRepo.save(movimiento);

        // Verificar si el stock es bajo y crear alerta
        logger.info("Verificando stock bajo para el producto: {}, Stock actual: {}", producto.getNombre(), stock.getStock());
         // Aquí puedes definir un umbral mínimo para generar alertas
         // Por ejemplo, si el stock es menor a 5 unidades
        if (stock.getStock() < 5) { // Aquí 5 es el umbral mínimo. Puedes parametrizarlo.
            AlertaInventario alerta = new AlertaInventario();
            alerta.setProductoId(producto.getId());
            alerta.setSucursalId(sucursalId);
            alerta.setStockActual(stock.getStock());
            alerta.setMensaje("Stock bajo para producto " + producto.getNombre());
            alerta.setFechaHora(LocalDateTime.now());
            alertaRepo.save(alerta);
        }
        logger.info("Movimiento registrado correctamente: {}", movimiento);
         // Retornar el movimiento registrado
        return movimiento;
    }

        
    public List<Movimiento> listarMovimientos() {
        logger.info("Listando todos los movimientos de inventario");
         // Obtener todos los movimientos
        List<Movimiento> movimientos = movimientoRepo.findAll();
        if (movimientos.isEmpty()) {
            logger.warn("No se encontraron movimientos de inventario");
            return List.of();
        }
        return movimientos;
    }

    public Movimiento obtenerMovimientoPorId(Long id) {
        logger.info("Obteniendo movimiento con ID: {}", id);
        if (id == null) {
            logger.error("ID de movimiento no proporcionado");
            throw new IllegalArgumentException("El ID de movimiento no puede ser nulo");
        }
         // Buscar el movimiento por ID
        logger.info("Buscando movimiento en la base de datos");
        if (!movimientoRepo.existsById(id)) {
            logger.error("Movimiento no encontrado con ID: {}", id);
            throw new RuntimeException("Movimiento no encontrado");
        }
         // Retornar el movimiento encontrado
        return movimientoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }

    public List<Movimiento> listarPorSucursal(Long sucursalId) {
        logger.info("Listando movimientos para la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
         // Buscar movimientos por ID de sucursal
        return movimientoRepo.findBySucursalId(sucursalId);
    }

    public List<Movimiento> listarPorProducto(Long productoId) {
        logger.info("Listando movimientos para el producto con ID: {}", productoId);
        if (productoId == null) {
            logger.error("ID de producto no proporcionado");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }
        return movimientoRepo.findByProductoId(productoId);
    }

    public void eliminarMovimiento(Long id) {
        logger.info("Eliminando movimiento con ID: {}", id);
        if (id == null) {
            logger.error("ID de movimiento no proporcionado");
            throw new IllegalArgumentException("El ID de movimiento no puede ser nulo");
        }
         // Verificar si el movimiento existe antes de eliminar
        if (!movimientoRepo.existsById(id)) {
            logger.error("Movimiento no encontrado con ID: {}", id);
            throw new RuntimeException("Movimiento no encontrado para eliminar");
        }
        movimientoRepo.deleteById(id);
    }
}
