package cl.perfulandia.inventario.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.perfulandia.inventario.modelo.AlertaInventario;

/*
 * AlertaRepositorio.java
 * Este repositorio maneja las operaciones de acceso a datos para las alertas de inventario.
 * Permite buscar alertas por sucursal, producto o ambos.
 */
@Repository
public interface AlertaRepositorio extends JpaRepository<AlertaInventario, Long> {
    /*
     * Busca alertas de inventario por ID de sucursal.
     * @param sucursalId El ID de la sucursal para filtrar las alertas.
     * @return Una lista de AlertaInventario asociadas a la sucursal especificada.
     */ 
    List<AlertaInventario> findBySucursalId(Long sucursalId);
    /*
     * Busca alertas de inventario por ID de producto.
     * @param productoId El ID del producto para filtrar las alertas.
     * @return Una lista de AlertaInventario asociadas al producto especificado.
     */
    List<AlertaInventario> findByProductoId(Long productoId);
    /*
     * Busca alertas de inventario por ID de sucursal y ID de producto.
     * @param sucursalId El ID de la sucursal para filtrar las alertas.
     * @param productoId El ID del producto para filtrar las alertas.
     * @return Una lista de AlertaInventario asociadas a la sucursal y producto especificados.
     */
    List<AlertaInventario> findBySucursalIdAndProductoId(Long sucursalId, Long productoId);
}