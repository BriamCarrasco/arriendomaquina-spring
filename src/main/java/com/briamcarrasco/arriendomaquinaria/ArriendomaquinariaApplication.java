package com.briamcarrasco.arriendomaquinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Arriendo Maquinaria.
 * Esta clase inicia la aplicación Spring Boot para el sistema de arriendo de
 * maquinaria.
 * 
 * @author Briam Carrasco | Vanessa Suazo
 * @since 1.0
 */
@SpringBootApplication
public class ArriendomaquinariaApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(ArriendomaquinariaApplication.class, args);
	}

}