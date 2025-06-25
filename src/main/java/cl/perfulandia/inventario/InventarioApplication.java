package cl.perfulandia.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// InventarioApplication.java
// Este es el punto de entrada principal para la aplicación de inventario.
@SpringBootApplication
@EnableFeignClients
public class InventarioApplication {
	/**
	 * Método principal que inicia la aplicación de inventario.
	 * @param args Argumentos de línea de comandos.
	 */
	public static void main(String[] args) {
		SpringApplication.run(InventarioApplication.class, args);
	}

}
