package cl.perfulandia.inventario.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.repositorio.ProductoRepositorio;
import lombok.RequiredArgsConstructor;

/**
 * ProductoService.java
 * Este servicio maneja las operaciones relacionadas con los productos en el inventario.
 * Permite listar, obtener, crear, actualizar y eliminar productos.
 */
@Service
@RequiredArgsConstructor
public class ProductoService {
    /**
     * Logger para registrar eventos en el servicio de productos.
     * Utilizado para depuración y seguimiento de operaciones.
     */
    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
    /**
     * Repositorio para acceder a los datos de los productos.
     * Proporciona métodos para realizar operaciones CRUD sobre la entidad Producto.
     */
    private final ProductoRepositorio productoRepository;

    /**
     * Constructor del servicio de productos.
     * Inyecta el repositorio de productos para realizar operaciones de acceso a datos.
     */
    public List<Producto> listarProductos() {
        logger.info("Listando todos los productos");
        if (productoRepository.count() == 0) {
            logger.warn("No se encontraron productos en la base de datos");
            return List.of();
        }
        return productoRepository.findAll();
    }

    /**
     * Obtiene un producto por su ID.
     * @param sucursalId El ID del producto a buscar.
     * @return El producto encontrado o una excepción si no se encuentra.
     */
    public Producto obtenerProductoPorId(Long sucursalId) {
        logger.info("Obteniendo producto con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de producto no proporcionado");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }
        return productoRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    /**
     * Crea un nuevo producto en el inventario.
     * @param producto El producto a crear.
     * @return El producto creado.
     */
    public Producto crearProducto(Producto producto) {
        logger.info("Creando nuevo producto: {}", producto);
        if (producto.getNombre() == null || producto.getPrecio() <= 0) {
            logger.error("Datos de producto inválidos: {}", producto);
            throw new IllegalArgumentException("El nombre y el precio del producto son obligatorios y deben ser válidos");
        }
        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto existente en el inventario.
     * @param sucursalId El ID del producto a actualizar.
     * @param productoActualizado Los nuevos datos del producto.
     * @return El producto actualizado.
     */
    public Producto actualizarProducto(Long sucursalId, Producto productoActualizado) {
        logger.info("Actualizando producto con ID: {}", sucursalId);
        if (sucursalId == null || productoActualizado == null) {
            logger.error("ID de producto o datos de actualización no proporcionados");
            throw new IllegalArgumentException("El ID de producto y los datos de actualización son obligatorios");
        }
        Producto producto = productoRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setStock(productoActualizado.getStock());
        producto.setStockMinimo(productoActualizado.getStockMinimo());
        producto.setPrecio(productoActualizado.getPrecio());

        return productoRepository.save(producto);
    }

    /**
     * Elimina un producto del inventario por su ID.
     * @param sucursalId El ID del producto a eliminar.
     */
    public void eliminarProducto(Long sucursalId) {
        logger.info("Eliminando producto con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de producto no proporcionado");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }
        productoRepository.deleteById(sucursalId);
    }
}
