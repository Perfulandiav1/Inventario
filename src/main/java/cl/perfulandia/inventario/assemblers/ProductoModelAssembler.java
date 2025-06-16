package cl.perfulandia.inventario.assemblers;

import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.controlador.ProductoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {
    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoController.class).obtenerProductoPorId(producto.getId())).withRel("Producto por ID"),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("Todos los productos"),
            linkTo(methodOn(ProductoController.class).crearProducto(producto)).withRel("Crear producto"),
            linkTo(methodOn(ProductoController.class).actualizarProducto(producto.getId(), producto)).withRel("Actualizar producto"),
            linkTo(methodOn(ProductoController.class).eliminarProducto(producto.getId())).withRel("Eliminar producto")
        );
    }
}
