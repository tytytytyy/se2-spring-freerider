package de.freerider.repository;

import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.freerider.model.Customer;
import de.freerider.model.DataFactory;


@Component		// only @Component instance of type CrudRepository<Customer, String> allowed
				// remove @Component from other instances
//
class CustomerRepository_JSON extends CustomerRepository_Impl {
	//
	private final Path path = Paths.get( "data/customers.json" );
	//
	private final ObjectMapper mapper = new ObjectMapper();		// JSON/jackson object mapper
	//
	private int repositoryStatus = 0;	// 0: initial; 1: ready, -1: error

	@Autowired
	private DataFactory dataFactory;


	@EventListener(ApplicationReadyEvent.class)
	public void doWhenApplicationReady() {
		probeLoadJSON();	// test repository ready; if not, load repository
	}


	@Override
	public <S extends Customer> S save( S entity ) {
		S result = super.save( entity );
		saveJSON();		// repository changed: save to JSON
		return result;
	}

	@Override
	public <S extends Customer> Iterable<S> saveAll( Iterable<S> entities ) {
		Iterable<S> result = super.saveAll( entities );
		saveJSON();		// repository changed: save to JSON
		return result;
	}

	@Override
	public long count() {
		probeLoadJSON();	// test repository ready; if not, load repository
		return super.count();
	}

	@Override
	public void deleteById( String id ) {
		super.deleteById( id );
		saveJSON();		// repository changed: save to JSON
	}

	@Override
	public void delete( Customer entity ) {
		super.delete( entity );
		saveJSON();		// repository changed: save to JSON
	}

	@Override
	public void deleteAllById( Iterable<? extends String> ids ) {
		super.deleteAllById( ids );
		saveJSON();		// repository changed: save to JSON
	}

	@Override
	public void deleteAll( Iterable<? extends Customer> entities ) {
		super.deleteAll( entities );
		saveJSON();		// repository changed: save to JSON
	}

	@Override
	public void deleteAll() {
		super.deleteAll();
		saveJSON();		// repository changed: save to JSON
	}


	/*
	 * Private methods
	 */

	private void saveJSON() {
		if( repositoryStatus == 0 ) {
			probeLoadJSON();	// test repository ready; if not, load repository
		}
		if( repositoryStatus == 1 ) {
			try {
				if( path.getNameCount() > 1 ) {
					Path dirPath = path.getParent();
					Files.createDirectories( dirPath );
					log( dirPath, " created" );
				}
				//mapper.writeValue( path.toFile(), this.findAll() );
				mapper.writerWithDefaultPrettyPrinter()	// pretty-print JSON
					.writeValue( path.toFile(),
						this.findAll()	// save as: Iterable<Customer>
				);
				log( path, " saved" );

			} catch( IOException e ) {
				log( "Failed to save: ", e );
			}
		}
	}

	private void probeLoadJSON() {
		//
		if( repositoryStatus == 0 ) {
			//
			if( Files.notExists( path ) ) {
				// JSON file not found, build new repository with some mock data
				Iterable<Customer> customers = dataFactory.createCustomerMockData();
				repositoryStatus = 1;
				super.deleteAll();
				super.saveAll( customers );

			} else {
				// JSON file found, load repository from JSON
				try {
					@SuppressWarnings("unchecked")
					// JSON was saved as type: Iterable<Customer>
					Iterable<HashMap<String,Object>> deserializedObjects =
						mapper.readValue( path.toFile(), Iterable.class );
					//
					if( deserializedObjects != null ) {
						for( HashMap<String,Object> map : deserializedObjects ) {
							try {
								Customer customer = dataFactory.createCustomer( map );
								//
								super.save( customer );
								//
								log( "--> customer loaded: ", customer );

							} catch( IllegalArgumentException iax ) {
								log( "IllegalArgumentException, Failed to deserialize Customer object: ", iax );
							}
						}
						log( path, " loaded: " + deserializedObjects );
						repositoryStatus = 1;	// repository ready
					}

				} catch( JsonMappingException jex ) {
					log( "JsonMappingException, Failed to load: ", jex );

				} catch( JsonParseException jpx ) {
					log( "JsonParseException, Failed to load: ", jpx );

				} catch( IOException e ) {
					log( "IOException, Failed to load: ", e );
				}

				repositoryStatus = repositoryStatus == 1? 1 : -1;
			}
		}
	}

	private void log( String msg, Customer customer ) {
		System.out.println( msg + ( customer == null? "" : customer.getId() + "; " +
			customer.getLastName() + ", " + customer.getFirstName() ) );
	}

	private void log( Path path, String msg ) {
		System.out.println( path.toString().replace( '\\', '/' ) + msg );
	}

	private void log( String msg, Exception ex ) {
		System.err.println( msg + ex.getMessage().replace( '\\', '/' ) );
	}

}
