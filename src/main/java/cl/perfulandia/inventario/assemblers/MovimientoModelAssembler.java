package cl.perfulandia.inventario.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import cl.perfulandia.inventario.controlador.MovimientoController;
import cl.perfulandia.inventario.modelo.Movimiento;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class MovimientoModelAssembler implements RepresentationModelAssembler<Movimiento, EntityModel<Movimiento>> {

    @Override
    public EntityModel<Movimiento> toModel(Movimiento movimiento) {
        return EntityModel.of(movimiento,
            linkTo(methodOn(MovimientoController.class).obtenerMovimiento(movimiento.getId())).withRel("Movimiento por ID"),
            linkTo(methodOn(MovimientoController.class).listarMovimientos()).withRel("Todos los movimientos"),
            linkTo(methodOn(MovimientoController.class).registrarMovimiento(movimiento)).withRel("Crear movimiento"),
            linkTo(methodOn(MovimientoController.class).eliminarMovimiento(movimiento.getId())).withRel("Eliminar movimiento")
        );
    }

}
