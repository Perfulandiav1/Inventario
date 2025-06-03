package cl.perfulandia.inventario.controlador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.perfulandia.inventario.dto.SucursalDTO;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.modelo.SucursalStock;
import cl.perfulandia.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioControlador {
    @Autowired
    public final InventarioService inventarioService;

    @GetMapping("/stock/{sucursalId}")
    public ResponseEntity<List<SucursalStock>> obtenerStock(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(inventarioService.obtenerStockPorSucursal(sucursalId));
    }

    @GetMapping("/alertas")
    public ResponseEntity<List<AlertaInventario>> listarAlertas() {
        return ResponseEntity.ok(inventarioService.listarAlertas());
    }

    @GetMapping("/sucursales")
    public ResponseEntity<List<SucursalDTO>> obtenerSucursales() {
        List<SucursalDTO> sucursales = inventarioService.obtenerSucursales();
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<SucursalDTO> obtenerSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(inventarioService.obtenerSucursalDetalle(sucursalId));
    }

}

