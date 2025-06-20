package cl.perfulandia.inventario.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.perfulandia.inventario.modelo.SucursalStock;

/**
 * SucursalStockRepositorio.java
 * Este repositorio maneja las operaciones de acceso a datos para el stock de sucursales.
 * Permite buscar stocks por ID de sucursal.
 */
@Repository
public interface SucursalStockRepositorio extends JpaRepository<SucursalStock, Long> {
    /**
     * Busca el stock de una sucursal específica por su ID.
     * @param sucursalId El ID de la sucursal para filtrar los stocks.
     * @return Una lista de SucursalStock asociada a la sucursal especificada.
     */
    List<SucursalStock> findBySucursalId(Long sucursalId);
}
