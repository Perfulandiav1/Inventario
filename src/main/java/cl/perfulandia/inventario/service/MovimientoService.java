package cl.perfulandia.inventario.service;

import java.time.LocalDateTime;
import java.util.List;
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
        if (!tipo.equalsIgnoreCase("ENTRADA") && !tipo.equalsIgnoreCase("SALIDA")) {
            throw new IllegalArgumentException("Tipo de movimiento inválido. Debe ser ENTRADA o SALIDA.");
        }

        // Buscar producto
        Producto producto = productoRepo.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Buscar o crear SucursalStock
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

        // Calcular nuevo stock
        int nuevoStock = stock.getStock() + (tipo.equalsIgnoreCase("ENTRADA") ? cantidad : -cantidad);
        if (nuevoStock < 0) {
            throw new IllegalStateException("Stock insuficiente para realizar la salida.");
        }
        stock.setStock(nuevoStock);
        sucursalStockRepo.save(stock);

        // Registrar el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setSucursalId(sucursalId);
        movimiento.setCantidad(cantidad);
        movimiento.setTipo(tipo);
        movimiento.setFechaHora(LocalDateTime.now());
        movimientoRepo.save(movimiento);

        // Verificar si el stock es bajo y crear alerta
        if (stock.getStock() < 5) { // Aquí 5 es el umbral mínimo. Puedes parametrizarlo.
            AlertaInventario alerta = new AlertaInventario();
            alerta.setProductoId(producto.getId());
            alerta.setSucursalId(sucursalId);
            alerta.setStockActual(stock.getStock());
            alerta.setMensaje("Stock bajo para producto " + producto.getNombre());
            alerta.setFechaHora(LocalDateTime.now());
            alertaRepo.save(alerta);
        }

        return movimiento;
    }

        
    public List<Movimiento> listarMovimientos() {
        return movimientoRepo.findAll();
    }

    public Movimiento obtenerMovimientoPorId(Long id) {
        return movimientoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }

    public List<Movimiento> listarPorSucursal(Long sucursalId) {
        return movimientoRepo.findBySucursalId(sucursalId);
    }

    public List<Movimiento> listarPorProducto(Long productoId) {
        return movimientoRepo.findByProductoId(productoId);
    }

    public void eliminarMovimiento(Long id) {
        if (!movimientoRepo.existsById(id)) {
            throw new RuntimeException("Movimiento no encontrado para eliminar");
        }
        movimientoRepo.deleteById(id);
    }
}
