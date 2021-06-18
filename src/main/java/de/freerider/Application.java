package de.freerider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import de.freerider.datamodel.Customer;
import de.freerider.repository.CrudRepository;


@SpringBootApplication
public class Application {

	@Autowired
	@Qualifier("CustomerRepository_Proxy")
	CrudRepository<Customer,String> customerRepository;


	Application() {
		log( "Constructor()" );
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			log( "CommandLineRunner runner()" );
			//
			long count = customerRepository.count();	// triggers loading data
			System.out.println( "CustomerRepository.count() -> " + count );
			//
			String id = "C020301";
			customerRepository.findById( id ).ifPresentOrElse( c -> { log( "Customer found", c ); }, () -> {
				log( "No Customer found for id: " + id, (Customer)null );
			});
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


	public static void log( String msg, Customer customer ) {
		System.out.println( msg + ( customer == null? "" : ": id:" + customer.getId() + ", " +
			customer.getLastName() + ", " + customer.getFirstName() + ", contact:" +
				customer.getContact() + ", status:\"" + customer.getStatus() + "\"" ) );
	}

	public static void log( String msg ) {
		//System.err.println( Application.class.getSimpleName() + "::" + msg );
	}

}
