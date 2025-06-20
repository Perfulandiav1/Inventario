package cl.perfulandia.inventario.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.perfulandia.inventario.modelo.Producto;

/*
 * ProductoRepositorio.java
 * Este repositorio maneja las operaciones de acceso a datos para los productos.
 * Permite realizar operaciones CRUD sobre la entidad Producto.
 */
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> { 
    
}
