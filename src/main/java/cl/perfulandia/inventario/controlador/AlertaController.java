package cl.perfulandia.inventario.controlador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.service.AlertaInventarioService;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    @Autowired
    private AlertaInventarioService alertaService;

    @GetMapping("/listar")
    public ResponseEntity<List<AlertaInventario>> listarTodas() {
        return ResponseEntity.ok(alertaService.obtenerTodasAlertas());
    }

    @GetMapping("/obtener/sucursal/{sucursalId}")
    public ResponseEntity<List<AlertaInventario>> listarPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(alertaService.obtenerAlertasPorSucursal(sucursalId));
    }

    @GetMapping("/obtener/producto/{productoId}")
    public ResponseEntity<List<AlertaInventario>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(alertaService.obtenerAlertasPorProducto(productoId));
    }

    @GetMapping("/obtener/sucursal/{sucursalId}/producto/{productoId}")
    public ResponseEntity<List<AlertaInventario>> listarPorSucursalYProducto(@PathVariable Long sucursalId, @PathVariable Long productoId) {
        return ResponseEntity.ok(alertaService.obtenerAlertasPorSucursalYProducto(sucursalId, productoId));
    }
}

