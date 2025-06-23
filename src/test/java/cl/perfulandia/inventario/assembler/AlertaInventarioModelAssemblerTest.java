package cl.perfulandia.inventario.assembler;

import cl.perfulandia.inventario.assemblers.AlertaInventarioModelAssembler;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;


import static org.assertj.core.api.Assertions.assertThat;

class AlertaInventarioModelAssemblerTest {

    private AlertaInventarioModelAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new AlertaInventarioModelAssembler();
    }

    @Test
    void toModel_debeAgregarLinksCorrectos() {
        // Arrange
        AlertaInventario alerta = new AlertaInventario();
        alerta.setId(10L);
        alerta.setSucursalId(5L);
        alerta.setProductoId(7L);

        // Act
        EntityModel<AlertaInventario> model = assembler.toModel(alerta);

        // Assert
        assertThat(model.getContent()).isEqualTo(alerta);

        // Verifica que los enlaces existen y contienen los IDs correctos
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Alerta por ID") &&
                link.getHref().contains("/api/alertas/" + alerta.getId()));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Todas las alertas"));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Alertas por sucursal y producto") &&
                link.getHref().contains("/api/alertas/sucursal/" + alerta.getSucursalId() + "/producto/" + alerta.getProductoId()));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("Alertas por producto") &&
                link.getHref().contains("/api/alertas/producto/" + alerta.getProductoId()));
        assertThat(model.getLinks()).anyMatch(link -> link.getRel().value().equals("sucursal") &&
                link.getHref().contains("/api/inventario/sucursal/" + alerta.getSucursalId()));
    }
}
