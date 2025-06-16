package cl.perfulandia.inventario.controlador;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import cl.perfulandia.inventario.assemblers.AlertaInventarioModelAssembler;
import cl.perfulandia.inventario.assemblers.SucursalStockModelAssembler;
import cl.perfulandia.inventario.dto.SucursalDTO;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.modelo.SucursalStock;
import cl.perfulandia.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Operaciones relacionadas con el inventario")
public class InventarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(InventarioControlador.class);
    @Autowired
    public final InventarioService inventarioService;
    private final AlertaInventarioModelAssembler alertaInventarioModelAssembler;

    public InventarioControlador(InventarioService inventarioService, SucursalStockModelAssembler sucursalStockModelAssembler) {
        this.inventarioService = inventarioService;
        this.alertaInventarioModelAssembler = new AlertaInventarioModelAssembler();
    }

    @Operation(summary = "Obtener stock por ID de sucursal", description = "Obtiene el stock de productos para una sucursal específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock obtenido correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalStock.class))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/stock/{sucursalId}")
    public EntityModel<List<SucursalStock>> obtenerStock(@PathVariable Long sucursalId) {
        logger.info("Obteniendo stock para la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        List<SucursalStock> stock = inventarioService.obtenerStockPorSucursal(sucursalId);
        if (stock == null) {
            logger.warn("No se encontró stock para la sucursal con ID: {}", sucursalId);
            return EntityModel.of(Collections.<SucursalStock>emptyList())
                    .add(linkTo(methodOn(InventarioControlador.class).obtenerStock(sucursalId)).withSelfRel());
        }
        logger.info("Stock obtenido correctamente para la sucursal con ID: {}", sucursalId);
        return EntityModel.of(stock)
                .add(linkTo(methodOn(InventarioControlador.class).obtenerStock(sucursalId)).withSelfRel());
    }
    //
    @Operation(summary = "Listar alertas de inventario", description = "Obtiene una lista de alertas de inventario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alertas obtenidas correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertaInventario.class)))
    })
    @GetMapping("/alertas")
    public CollectionModel<EntityModel<AlertaInventario>> listarAlertas() {
        logger.info("Listando alertas de inventario");
        List<AlertaInventario> alertas = inventarioService.listarAlertas();
        if (alertas.isEmpty()) {
            logger.warn("No se encontraron alertas de inventario");
            return CollectionModel.<EntityModel<AlertaInventario>>of(Collections.emptyList())
                    .add(linkTo(methodOn(InventarioControlador.class).listarAlertas()).withSelfRel());
        }
        List<EntityModel<AlertaInventario>> alertasModel = alertas.stream()
                .map(alertaInventarioModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(alertasModel)
                .add(linkTo(methodOn(InventarioControlador.class).listarAlertas()).withSelfRel());
    }

    @Operation(summary = "Listar sucursales", description = "Obtiene una lista de todas las sucursales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursales obtenidas correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalDTO.class)))
    })
    @GetMapping("/sucursales")
    public ResponseEntity<List<SucursalDTO>> obtenerSucursales() {
        logger.info("Obteniendo lista de sucursales");
        if (inventarioService.obtenerSucursales().isEmpty()) {
            logger.warn("No se encontraron sucursales");
            return ResponseEntity.noContent().build();
        }
        List<SucursalDTO> sucursales = inventarioService.obtenerSucursales();
        logger.info("Total de sucursales encontradas: {}", sucursales.size());
        if (sucursales.isEmpty()) {
            logger.warn("No se encontraron sucursales");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sucursales);
    }
    //
    @Operation(summary = "Obtener detalle de sucursal", description = "Obtiene el detalle de una sucursal específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<SucursalDTO> obtenerSucursal(@PathVariable Long sucursalId) {
        logger.info("Obteniendo detalle de la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        logger.info("Obteniendo detalle de la sucursal con ID: {}", sucursalId);
        SucursalDTO sucursal = inventarioService.obtenerSucursalDetalle(sucursalId);
        return ResponseEntity.ok(sucursal);
    }

}

