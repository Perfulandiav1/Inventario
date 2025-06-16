package cl.perfulandia.inventario.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import cl.perfulandia.inventario.controlador.AlertaController;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AlertaInventarioModelAssembler implements RepresentationModelAssembler<AlertaInventario, EntityModel<AlertaInventario>> {

    @Override
    public EntityModel<AlertaInventario> toModel(AlertaInventario alertaInventario) {
        return EntityModel.of(alertaInventario,
            linkTo(methodOn(AlertaController.class).obtenerPorId(alertaInventario.getId())).withRel("Alerta por ID"),
            linkTo(methodOn(AlertaController.class).listarTodas()).withRel("Todas las alertas"),
            linkTo(methodOn(AlertaController.class).listarPorSucursalYProducto(
                alertaInventario.getSucursalId(),
                alertaInventario.getProductoId())).withRel("Alertas por sucursal y producto"),
            linkTo(methodOn(AlertaController.class).listarPorProducto(alertaInventario.getProductoId())).withRel("Alertas por producto")
        );
    }

}
