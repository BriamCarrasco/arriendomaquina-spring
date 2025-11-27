package com.briamcarrasco.arriendomaquinaria;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
class ArriendomaquinariaApplicationTests {

	@Test
	void contextLoads() {
		// Verifica que el contexto de Spring Boot se carga correctamente.
	}

	@Test
	void main_runsWithoutExceptions() {
		assertDoesNotThrow(() -> ArriendomaquinariaApplication.main(new String[] {}));
	}
}
