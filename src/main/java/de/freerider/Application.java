package de.freerider;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class Application {

	Application() {
		log( "Constructor()" );
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			log( "CommandLineRunner runner()" );
		};
	}

	@EventListener(ApplicationReadyEvent.class)
	void doWhenApplicationReady() {
		log( "doWhenApplicationReady()" );
	}

	public static void main( String[] args ) {
		log( "main()" );
		//
		SpringApplication.run( Application.class, args );
		//
		log( "main() leaving" );
	}


	public static void log( String msg ) {
		//System.err.println( Application.class.getSimpleName() + "::" + msg );
	}

}
