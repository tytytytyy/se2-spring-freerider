package de.freerider.datasource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.freerider.datamodel.Customer;
import de.freerider.datamodel.DataFactory;


/**
 * Data source implementation with entities saved as JSON-data and
 * loaded from JSON-data.
 * 
 * @author svgr2
 *
 */
public class DataSourceCustomers_JSON implements DataSource<Customer> {
	//
	@Autowired
	private DataFactory dataFactory;
	//
	// data path from application.properties or :default
	@Value("${de.freerider.data.customers.json:data/customers.json}")
	private Path dataPath = null;	// auto-initialized from property value
	//
	private final ObjectMapper mapper = new ObjectMapper();		// JSON/jackson object mapper


	@Override
	public void saveData( Iterable<Customer> entities ) throws IOException {
		//
		if( dataPath.getNameCount() > 1 ) {
			Path dirPath = dataPath.getParent();
			Files.createDirectories( dirPath );
			log( dirPath, " created" );
		}
		// pretty-write JSON with lines and indents
		//mapper.writeValue( path.toFile(), this.findAll() );
		mapper.writerWithDefaultPrettyPrinter()
			.writeValue( dataPath.toFile(), entities );
		//
		log( dataPath, " saved" );
	}


	@Override
	public void loadData( DataCollector<Customer> collector ) throws IOException {
		boolean error = true;
		//
		if( Files.exists( dataPath ) ) {
			// JSON file found, load repository from JSON
			try {
				@SuppressWarnings("unchecked")
				//
				// JSON was saved as type: Iterable<Customer>
				Iterable<HashMap<String,Object>> deserializedObjects =
					mapper.readValue( dataPath.toFile(), Iterable.class );
				//
				if( deserializedObjects != null ) {
					List<Customer> customers = new ArrayList<Customer>();
					for( HashMap<String,Object> map : deserializedObjects ) {
						try {
							Customer customer = dataFactory.createCustomer( map );
							customers.add( customer );
							//
							log( "--> customer loaded: ", customer );

						} catch( IllegalArgumentException iax ) {
							log( "IllegalArgumentException, Failed to deserialize Customer object: ", iax );
						}
					}
					collector.collect( customers );
					error = false;
					//
					log( dataPath, " loaded: " + deserializedObjects );
				}

			} catch( JsonMappingException jex ) {
				log( "JsonMappingException, Failed to load: ", jex );

			} catch( JsonParseException jpx ) {
				log( "JsonParseException, Failed to load: ", jpx );

			} catch( IOException e ) {
				log( "IOException, Failed to load: ", e );
			}
		}
		if( error ) {
			throw new IOException( "Error loading " + dataPath.toString() );
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
