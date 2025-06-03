package cl.perfulandia.inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.repositorio.ProductoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepositorio productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Long sucursalId) {
        return productoRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long sucursalId, Producto productoActualizado) {
        Producto producto = productoRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setStock(productoActualizado.getStock());
        producto.setStockMinimo(productoActualizado.getStockMinimo());
        producto.setPrecio(productoActualizado.getPrecio());

        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long sucursalId) {
        productoRepository.deleteById(sucursalId);
    }
}
