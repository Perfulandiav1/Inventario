package cl.perfulandia.inventario.assembler;

import cl.perfulandia.inventario.assemblers.MovimientoModelAssembler;
import cl.perfulandia.inventario.modelo.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test para el ensamblador de modelos de Movimiento.
 */
class MovimientoModelAssemblerTest {
     /*
        * Este test verifica que el ensamblador de modelos de Movimiento
        * agrega los enlaces correctos al modelo.
        */
    private MovimientoModelAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new MovimientoModelAssembler();
    }
    /**
     * Verifica que el método toModel agrega los enlaces correctos al modelo de Movimiento.
     */
    @Test
    void toModel_debeAgregarLinksCorrectos() {
        // Arrange
        Movimiento movimiento = new Movimiento();
        movimiento.setId(10L);

        // Act
        EntityModel<Movimiento> model = assembler.toModel(movimiento);

        // Assert
        assertThat(model.getContent()).isEqualTo(movimiento);

        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Movimiento por ID")
                && link.getHref().contains("/api/movimientos/obtener/10"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Todos los movimientos"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Crear movimiento"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Eliminar movimiento"));
    }
}