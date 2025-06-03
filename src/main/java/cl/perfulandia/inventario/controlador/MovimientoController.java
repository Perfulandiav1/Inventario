package cl.perfulandia.inventario.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.perfulandia.inventario.dto.MovimientoRequest;
import cl.perfulandia.inventario.modelo.Movimiento;
import cl.perfulandia.inventario.service.MovimientoService;
@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping("/registrar")
    public ResponseEntity<Movimiento> registrarMovimiento(@RequestBody MovimientoRequest request) {
        Movimiento movimiento = movimientoService.registrarMovimiento(
            request.getSucursalId(), request.getProductoId(), request.getCantidad(), request.getTipo());
        return ResponseEntity.ok(movimiento);
    }

    @GetMapping("/Listar")
    public ResponseEntity<List<Movimiento>> listarMovimientos() {
        return ResponseEntity.ok(movimientoService.listarMovimientos());
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<Movimiento> obtenerMovimiento(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientoPorId(id));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Movimiento>> movimientosPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(movimientoService.listarPorSucursal(sucursalId));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Movimiento>> movimientosPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(movimientoService.listarPorProducto(productoId));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}
