package cl.perfulandia.inventario.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.perfulandia.inventario.modelo.SucursalStock;

@Repository
public interface SucursalStockRepositorio extends JpaRepository<SucursalStock, Long> {
    List<SucursalStock> findBySucursalId(Long sucursalId);
}
