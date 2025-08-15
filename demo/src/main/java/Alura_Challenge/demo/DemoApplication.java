package Alura_Challenge.demo;

import Alura_Challenge.demo.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private final Principal principal;

	// Spring inyectará Principal automáticamente
	public DemoApplication(Principal principal) {
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		principal.muestraElMenu();
	}
}
