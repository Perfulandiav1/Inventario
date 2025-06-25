package cl.perfulandia.inventario.assembler;

import cl.perfulandia.inventario.assemblers.SucursalStockModelAssembler;
import cl.perfulandia.inventario.modelo.SucursalStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test para el ensamblador de modelos de SucursalStock.
 */
class SucursalStockModelAssemblerTest {
    
    private SucursalStockModelAssembler assembler;
    /*
     * Este test verifica que el ensamblador de modelos de SucursalStock
     * agrega los enlaces correctos al modelo.
     */
    @BeforeEach
    void setUp() {
        assembler = new SucursalStockModelAssembler();
    }
    /**
     * Verifica que el método toModel agrega los enlaces correctos al modelo de SucursalStock.
     */
    @Test
    void toModel_debeAgregarLinksCorrectos() {
        // Arrange
        SucursalStock sucursalStock = new SucursalStock();
        sucursalStock.setId(3L);

        // Act
        EntityModel<SucursalStock> model = assembler.toModel(sucursalStock);

        // Assert
        assertThat(model.getContent()).isEqualTo(sucursalStock);

        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Sucursal por ID")
                && link.getHref().contains("/api/inventario/sucursal/3"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Todas las sucursales"));
    }
}
