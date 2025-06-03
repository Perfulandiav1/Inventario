package cl.perfulandia.inventario.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.perfulandia.inventario.modelo.AlertaInventario;

@Repository
public interface AlertaRepositorio extends JpaRepository<AlertaInventario, Long> { 
    List<AlertaInventario> findBySucursalId(Long sucursalId);
    List<AlertaInventario> findByProductoId(Long productoId);
    List<AlertaInventario> findBySucursalIdAndProductoId(Long sucursalId, Long productoId);
}