package cl.perfulandia.inventario.assembler;

import cl.perfulandia.inventario.assemblers.ProductoModelAssembler;
import cl.perfulandia.inventario.modelo.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoModelAssemblerTest {

    private ProductoModelAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new ProductoModelAssembler();
    }

    @Test
    void toModel_debeAgregarLinksCorrectos() {
        // Arrange
        Producto producto = new Producto();
        producto.setId(5L);

        // Act
        EntityModel<Producto> model = assembler.toModel(producto);

        // Assert
        assertThat(model.getContent()).isEqualTo(producto);

        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Producto por ID")
                && link.getHref().contains("/api/productos/obtener/5"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Todos los productos"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Crear producto"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Actualizar producto"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Eliminar producto"));
    }
}
