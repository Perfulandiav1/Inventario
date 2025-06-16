package cl.perfulandia.inventario.controlador;

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

import cl.perfulandia.inventario.assemblers.AlertaInventarioModelAssembler;
import cl.perfulandia.inventario.modelo.AlertaInventario;
import cl.perfulandia.inventario.service.AlertaInventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/alertas")
@Tag(name = "Alertas", description = "Operaciones relacionadas con las alertas de inventario")
public class AlertaController {
    private static final Logger logger = LoggerFactory.getLogger(AlertaController.class);
    @Autowired
    private AlertaInventarioService alertaService;
    private AlertaInventarioModelAssembler alertaModelAssembler;

    public AlertaController(AlertaInventarioService alertaService, AlertaInventarioModelAssembler alertaModelAssembler) {
        this.alertaService = alertaService;
        this.alertaModelAssembler = alertaModelAssembler;
    }

    @Operation(summary = "Listar todas las alertas", description = "Obtiene una lista con todas las alertas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alertas obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertaInventario.class)))
    })
    @GetMapping("/listar")
    public CollectionModel<EntityModel<AlertaInventario>> listarTodas() {
        logger.info("Listando todas las alertas de inventario");
        List<AlertaInventario> alertas = alertaService.obtenerTodasAlertas();
        logger.info("Total de alertas encontradas: {}", alertas.size());
        List<EntityModel<AlertaInventario>> alertasModel = alertas.stream()
                .map(alertaModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(alertasModel)
                .add(linkTo(methodOn(AlertaController.class).listarTodas()).withSelfRel());
    }
    //
    @Operation(summary = "Obtener alerta por ID de sucursal", description = "Obtiene una alerta específica por su ID de sucursal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertaInventario.class))),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @GetMapping("/obtener/{id}")
    public EntityModel<AlertaInventario> obtenerPorId(@PathVariable Long sucursalId) {
        logger.info("Obteniendo alerta para la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            throw new IllegalArgumentException("El ID de sucursal no puede ser nulo");
        }
        AlertaInventario alerta = alertaService.obtenerAlertasPorSucursal(sucursalId).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada para la sucursal con ID: " + sucursalId));
        logger.info("Alerta obtenida correctamente para la sucursal con ID: {}", sucursalId);
        return alertaModelAssembler.toModel(alerta);
    }
    //
    @Operation(summary = "Listar alertas por producto id", description = "Obtiene una lista de alertas asociadas a un producto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alertas obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertaInventario.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron alertas para el producto")
    })
    @GetMapping("/obtener/producto/{productoId}")
    public ResponseEntity<List<AlertaInventario>> listarPorProducto(@PathVariable Long productoId) {
        logger.info("Listando alertas para el producto con ID: {}", productoId);
        if (productoId == null) {
            logger.error("ID de producto no proporcionado");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }   
        List<AlertaInventario> alertas = alertaService.obtenerAlertasPorProducto(productoId);
        logger.info("Alertas obtenidas correctamente para el producto con ID: {}", productoId);
        return ResponseEntity.ok(alertas);
    }
    //
    @Operation(summary = "Listar alertas por sucursal id y producto id", description = "Obtiene una lista de alertas asociadas a una sucursal específica y un producto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alertas obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertaInventario.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron alertas para la sucursal y producto especificados")
    })
    @GetMapping("/obtener/sucursal/{sucursalId}/producto/{productoId}")
    public ResponseEntity<List<AlertaInventario>> listarPorSucursalYProducto(@PathVariable Long sucursalId, @PathVariable Long productoId) {
        logger.info("Listando alertas para la sucursal con ID: {} y producto con ID: {}", sucursalId, productoId);
        if (sucursalId == null || productoId == null) {
            logger.error("ID de sucursal o producto no proporcionado");
            throw new IllegalArgumentException("Los IDs de sucursal y producto no pueden ser nulos");
        }
        List<AlertaInventario> alertas = alertaService.obtenerAlertasPorSucursalYProducto(sucursalId, productoId);
        logger.info("Alertas obtenidas correctamente para la sucursal con ID: {} y producto con ID: {}", sucursalId, productoId);
        return ResponseEntity.ok(alertas);
    }
}

