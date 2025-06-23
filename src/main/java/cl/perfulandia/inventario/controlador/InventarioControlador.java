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

/*
 * Controlador para manejar las operaciones relacionadas con el inventario.
 * Proporciona endpoints para obtener stock por sucursal, listar alertas de inventario y obtener detalles de sucursales.
 */
@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Operaciones relacionadas con el inventario")
public class InventarioControlador {

    /*
     * Logger para registrar eventos en el controlador de inventario.
     * Utilizado para depuración y seguimiento de operaciones.
     */
    private static final Logger logger = LoggerFactory.getLogger(InventarioControlador.class);
    /*
     * Servicio para manejar la lógica de negocio relacionada con el inventario.
     * Proporciona métodos para obtener stock por sucursal, listar alertas de inventario y obtener detalles de sucursales.
     */
    @Autowired
    public final InventarioService inventarioService;
    private final AlertaInventarioModelAssembler alertaInventarioModelAssembler;
    private final SucursalStockModelAssembler sucursalStockModelAssembler;

    /*
     * Ensamblador para convertir objetos AlertaInventario en EntityModel con enlaces HATEOAS.
     * Facilita la creación de respuestas enriquecidas con enlaces a las operaciones relacionadas.
     */	
    public InventarioControlador(InventarioService inventarioService, SucursalStockModelAssembler sucursalStockModelAssembler) {
        this.inventarioService = inventarioService;
        this.alertaInventarioModelAssembler = new AlertaInventarioModelAssembler();
        this.sucursalStockModelAssembler = sucursalStockModelAssembler;
    }

    /*
     * Endpoint para obtener el stock de productos por ID de sucursal.
     * Devuelve una lista de SucursalStock con enlaces HATEOAS.
     * 
     * @param sucursalId ID de la sucursal para la cual se desea obtener el stock.
     * @return EntityModel con la lista de SucursalStock y enlaces HATEOAS.
     */	
    @Operation(summary = "Obtener stock por ID de sucursal", description = "Obtiene el stock de productos para una sucursal específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock obtenido correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalStock.class))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    /*
     * Endpoint para obtener el stock de productos por ID de sucursal.
     * Devuelve una lista de SucursalStock con enlaces HATEOAS.
     */
    @GetMapping("/stock/{sucursalId}")
    public CollectionModel<EntityModel<SucursalStock>> obtenerStock(@PathVariable Long sucursalId) {
        logger.info("Obteniendo stock para la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        List<SucursalStock> stock = inventarioService.obtenerStockPorSucursal(sucursalId);
        List<EntityModel<SucursalStock>> stockModels = stock.stream()
            .map(sucursalStockModelAssembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(stockModels)
                .add(linkTo(methodOn(InventarioControlador.class).obtenerStock(sucursalId)).withSelfRel());
    }
    /*
     *  Endpoint para listar todas las alertas de inventario.
     *  Devuelve una colección de EntityModel con las alertas de inventario y enlaces HATEOAS.
     */
    @Operation(summary = "Listar alertas de inventario", description = "Obtiene una lista de alertas de inventario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alertas obtenidas correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertaInventario.class)))
    })

    /*
     * Endpoint para listar todas las alertas de inventario.
     * Devuelve una colección de EntityModel con las alertas de inventario y enlaces HATEOAS.
     */
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

    /*
     * Endpoint para listar todas las sucursales.
     * Devuelve una lista de SucursalDTO con enlaces HATEOAS.
     */	
    @Operation(summary = "Listar sucursales", description = "Obtiene una lista de todas las sucursales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursales obtenidas correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalDTO.class)))
    })
    /*
     * Endpoint para listar todas las sucursales.
     * Devuelve una lista de SucursalDTO con enlaces HATEOAS.
     */
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
    /*
     * Endpoint para obtener el detalle de una sucursal específica por su ID.
     * Retorna un objeto SucursalDTO con enlaces HATEOAS.
     */
    @Operation(summary = "Obtener detalle de sucursal", description = "Obtiene el detalle de una sucursal específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })

    /*
     * Endpoint para obtener el detalle de una sucursal específica por su ID.
     * Retorna un objeto SucursalDTO con enlaces HATEOAS.
     * 
     * @param sucursalId ID de la sucursal para la cual se desea obtener el detalle.
     * @return ResponseEntity con el detalle de la sucursal y enlaces HATEOAS.
     */
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

