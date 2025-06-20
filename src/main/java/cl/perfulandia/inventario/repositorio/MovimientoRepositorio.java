package cl.perfulandia.inventario.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.perfulandia.inventario.modelo.Movimiento;

/*
 * MovimientoRepositorio.java
 * Este repositorio maneja las operaciones de acceso a datos para los movimientos de inventario.
 * Permite buscar movimientos por sucursal o producto.
 */
@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long> {
    /*
     * Busca movimientos de inventario por ID de sucursal.
     * @param sucursalId El ID de la sucursal para filtrar los movimientos.
     * @return Una lista de Movimiento asociada a la sucursal especificada.
     */
    List<Movimiento> findBySucursalId(Long sucursalId);
    /*
     * Busca movimientos de inventario por ID de producto.
     * @param productoId El ID del producto para filtrar los movimientos.
     * @return Una lista de Movimiento asociada al producto especificado.
     */
    List<Movimiento> findByProductoId(Long productoId);
}

