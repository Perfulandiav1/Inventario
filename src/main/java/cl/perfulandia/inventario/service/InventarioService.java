package cl.perfulandia.inventario.service;
import java.util.Collections;
import java.util.List;
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
    @Autowired
    private SucursalClient sucursalClient;
    private final SucursalStockRepositorio sucursalStockRepo;
    private final AlertaRepositorio alertaRepo;

    

    public List<SucursalStock> obtenerStockPorSucursal(Long sucursalId) {
        return sucursalStockRepo.findBySucursalId(sucursalId);
    }

    public SucursalDTO obtenerSucursalDetalle(Long sucursalId) {
        return sucursalClient.obtenerSucursalPorId(sucursalId);
    }

    /**
     * Listar todas las alertas.
     */
    public List<AlertaInventario> listarAlertas() {
        return alertaRepo.findAll();
    }

    public SucursalDTO obtenerSucursalPorId(Long id) {
        try {
            return sucursalClient.obtenerSucursalPorId(id);
        } catch (Exception e) {
            System.err.println("Error al llamar a Sucursal: " + e.getMessage());
            return null; // o lanza excepción personalizada
        }
    }

    public List<SucursalDTO> obtenerSucursales() {
        try {
            return sucursalClient.obtenerTodasSucursales();
        } catch (Exception e) {
            // Loguea o maneja el error según convenga
            System.err.println("Error al llamar a Sucursal: " + e.getMessage());
            return Collections.emptyList(); // o lanza excepción personalizada
        }
    }
}



