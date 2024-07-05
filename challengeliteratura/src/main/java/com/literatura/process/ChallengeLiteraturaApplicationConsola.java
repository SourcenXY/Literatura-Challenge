package com.literatura.process;
import com.literatura.process.Principal.Principal;
import com.literatura.process.repository.AutorRepository;
import com.literatura.process.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraturaApplicationConsola implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraturaApplicationConsola.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal libreria = new Principal(libroRepository, autorRepository);
		libreria.menu();
	}
}
