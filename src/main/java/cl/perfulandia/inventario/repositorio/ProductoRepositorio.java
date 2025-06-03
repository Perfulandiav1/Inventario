package cl.perfulandia.inventario.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.perfulandia.inventario.modelo.Producto;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> { 
    
}
