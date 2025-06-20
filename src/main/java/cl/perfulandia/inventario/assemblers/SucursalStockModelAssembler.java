package cl.perfulandia.inventario.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import cl.perfulandia.inventario.controlador.InventarioControlador;
import cl.perfulandia.inventario.modelo.SucursalStock;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/*
 * SucursalStockModelAssembler.java
 * Este ensamblador convierte objetos SucursalStock en EntityModel con enlaces HATEOAS.
 * Facilita la creación de respuestas enriquecidas con enlaces a las operaciones relacionadas.
 */
@Component
public class SucursalStockModelAssembler implements RepresentationModelAssembler<SucursalStock, EntityModel<SucursalStock>> {

    /*
     * Convierte un objeto SucursalStock en un EntityModel con enlaces HATEOAS.
     * Este método agrega enlaces a las operaciones relacionadas con la sucursal de stock.
     */
    @Override
    public EntityModel<SucursalStock> toModel(SucursalStock sucursalStock) {
        return EntityModel.of(sucursalStock,
            linkTo(methodOn(InventarioControlador.class).obtenerSucursal(sucursalStock.getId())).withRel("Sucursal por ID"),
            linkTo(methodOn(InventarioControlador.class).obtenerSucursales()).withRel("Todas las sucursales"));
    
    }

}
