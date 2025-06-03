package cl.perfulandia.inventario.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.repositorio.AlertaRepositorio;

@Service
public class AlertaInventarioService {

    @Autowired
    private AlertaRepositorio alertaRepo;

    public List<AlertaInventario> obtenerTodasAlertas() {
        return alertaRepo.findAll();
    }

    public List<AlertaInventario> obtenerAlertasPorSucursal(Long sucursalId) {
        return alertaRepo.findBySucursalId(sucursalId);
    }

    public List<AlertaInventario> obtenerAlertasPorProducto(Long productoId) {
        return alertaRepo.findByProductoId(productoId);
    }

    public List<AlertaInventario> obtenerAlertasPorSucursalYProducto(Long sucursalId, Long productoId) {
        return alertaRepo.findBySucursalIdAndProductoId(sucursalId, productoId);
    }

    public void crearAlerta(AlertaInventario alerta) {
        alerta.setFechaHora(LocalDateTime.now());
        alertaRepo.save(alerta);
    }
}
