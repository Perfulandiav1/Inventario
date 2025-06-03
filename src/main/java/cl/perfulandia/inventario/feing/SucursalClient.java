package cl.perfulandia.inventario.feing;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.perfulandia.inventario.dto.SucursalDTO;

@FeignClient(name = "sucursal-service", url = "http://localhost:8080")
public interface SucursalClient {

    @GetMapping("/api/sucursal")
    List<SucursalDTO> obtenerTodasSucursales();

    @GetMapping("/api/sucursal/{id}")
    SucursalDTO obtenerSucursalPorId(@PathVariable("id") Long id);
}


