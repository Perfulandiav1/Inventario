package cl.perfulandia.inventario.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.perfulandia.inventario.modelo.Movimiento;

@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findBySucursalId(Long sucursalId);
    List<Movimiento> findByProductoId(Long productoId);
}

