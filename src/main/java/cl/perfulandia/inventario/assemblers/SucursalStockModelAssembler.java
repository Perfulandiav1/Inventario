package cl.perfulandia.inventario.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import cl.perfulandia.inventario.controlador.InventarioControlador;
import cl.perfulandia.inventario.modelo.SucursalStock;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SucursalStockModelAssembler implements RepresentationModelAssembler<SucursalStock, EntityModel<SucursalStock>> {

    @Override
    public EntityModel<SucursalStock> toModel(SucursalStock sucursalStock) {
        return EntityModel.of(sucursalStock,
            linkTo(methodOn(InventarioControlador.class).obtenerSucursal(sucursalStock.getId())).withRel("Sucursal por ID"),
            linkTo(methodOn(InventarioControlador.class).obtenerSucursales()).withRel("Todas las sucursales"));
    
    }

}
