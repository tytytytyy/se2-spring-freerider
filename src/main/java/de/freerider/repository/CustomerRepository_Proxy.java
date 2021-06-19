package de.freerider.repository;

import java.io.IOException;
import java.util.Optional;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.freerider.datamodel.Customer;
import de.freerider.datamodel.DataFactory;
import de.freerider.datasource.DataSource;


/**
 * Implementation of CrudRepository<Customer, String> interface as a Proxy
 * that connects to an upstream Repository implementation and a downstream
 * data source.
 * 
 * @Component with @Qualifier to differentiate from other components of the
 * CrudRepository<Customer, String> interface.
 * 
 * @author svgr2
 *
 */
@Component
@Qualifier("CustomerRepository_Proxy")
public class CustomerRepository_Proxy implements CrudRepository<Customer, String> {
	//
	@Autowired
	@Qualifier("CustomerRepository_Impl")
	private CrudRepository<Customer, String> upstreamRepository;
	//
	@Autowired
	private DataFactory dataFactory;	// = DataFactory.getInstance();
	//
	private DataSource<Customer> downstreamDataSource = null;	// injected dependency

	/*
	 * status of CustomerRepository:
	 *  -  0: initial
	 *  -  1: repository ready and loaded with data
	 *  -  2: could not load data, load mock data instead
	 *  - -1: error
	 */
	private int status = 0;


	/**
	 * Inject DataSource as dependency.
	 * 
	 * @param downstream DataSource from which data is loaded and to which data is stored
	 */
	public void inject( DataSource<Customer> downstream ) {
		this.downstreamDataSource = downstream;
	}


	/*
	 * Public methods implementing CrudRepository<Customer, String> interface
	 */

	@Override
	public <S extends Customer> S save( S entity ) {
		guard( entity, "entity is null" );	// throws IllegalArgumentException when entity is null
		S previous = upstreamRepository.save( entity );			// save entity to actual CustomerRepository
		saveData( downstreamDataSource, upstreamRepository );	// save CustomerRepository downstream
		return previous;
	}

	@Override
	public <S extends Customer> Iterable<S> saveAll( Iterable<S> entities ) {
		guard( entities, "entities is null" );
		Iterable<S> result = upstreamRepository.saveAll( entities );
		saveData( downstreamDataSource, upstreamRepository );
		return result;
	}

	@Override
	public Optional<Customer> findById( String id ) {
		guard( id, "id is null" );
		loadData( downstreamDataSource, upstreamRepository );
		Optional<Customer> result = upstreamRepository.findById( id );
		return result;
	}

	@Override
	public boolean existsById( String id ) {
		guard( id, "id is null" );
		loadData( downstreamDataSource, upstreamRepository );
		boolean result = upstreamRepository.existsById( id );
		return result;
	}

	@Override
	public Iterable<Customer> findAll() {
		loadData( downstreamDataSource, upstreamRepository );
		Iterable<Customer> result = upstreamRepository.findAll();
		return result;
	}

	@Override
	public Iterable<Customer> findAllById( Iterable<String> ids ) {
		guard( ids, "ids is null" );
		loadData( downstreamDataSource, upstreamRepository );
		Iterable<Customer> result = upstreamRepository.findAllById( ids );
		return result;
	}

	@Override
	public long count() {
		loadData( downstreamDataSource, upstreamRepository );
		long result = upstreamRepository.count();
		return result;
	}

	@Override
	public void deleteById( String id ) {
		guard( id, "id is null" );
		loadData( downstreamDataSource, upstreamRepository );
		upstreamRepository.deleteById( id );
		saveData( downstreamDataSource, upstreamRepository );
	}

	@Override
	public void delete( Customer entity ) {
		guard( entity, "entity is null" );
		loadData( downstreamDataSource, upstreamRepository );
		upstreamRepository.delete( entity );
		saveData( downstreamDataSource, upstreamRepository );
	}

	@Override
	public void deleteAllById( Iterable<? extends String> ids ) {
		guard( ids, "ids is null" );
		loadData( downstreamDataSource, upstreamRepository );
		upstreamRepository.deleteAllById( ids );
		saveData( downstreamDataSource, upstreamRepository );
	}

	@Override
	public void deleteAll( Iterable<? extends Customer> entities ) {
		guard( entities, "entities is null" );
		loadData( downstreamDataSource, upstreamRepository );
		upstreamRepository.deleteAll( entities );
		saveData( downstreamDataSource, upstreamRepository );
	}

	@Override
	public void deleteAll() {
		upstreamRepository.deleteAll();
		saveData( downstreamDataSource, upstreamRepository );
	}


	/*
	 * Private methods
	 */

	/**
	 * Probe if data source is available (json file exists) and load data from source.
	 * If data source is not available, mock data is loaded from DataFactory.
	 * 
	 * @param downstream source from where data is loaded
	 * @param upstream destination for loaded data
	 */
	private void loadData( DataSource<Customer> downstream, CrudRepository<Customer, String> upstream ) {
		//
		if( guard( downstream, upstream ) ) {
			//
			switch( status ) {
			//
			case 0:		// initial
				try {
					downstream.loadData( dataFromSource -> {
						// load data into actual CustomerRepository
						upstream.saveAll( dataFromSource );
					});
					status = 1;

				} catch( IOException e ) {
					// data could not be loaded, load mock-data instead
					status = 2;
					loadData( downstream, upstream );	// load mock data
				}
				break;
			//
			case 1:		// ready, data loaded
				break;
			//
			case 2:		// load mock data
				try {
					// data could not be loaded, load mock-data instead
					Iterable<Customer> mockData = dataFactory.createCustomerMockData();
					upstreamRepository.saveAll( mockData );				// assigns id's for mock data
					downstreamDataSource.saveData( upstreamRepository.findAll() );	// write mock data with id's assigned
					status = 0;
					loadData( downstream, upstream );	// retry loading data

				} catch( IOException e1 ) {
					status = -1; 	// error
					// e.printStackTrace();
				}
				break;

			default:	// error
				System.err.println( this.getClass().getSimpleName() + ": probeData() Error loading data" );
				break;
			}
		}
	}


	/**
	 * Data from upstream repository is saved to downstream data source (json file).
	 * 
	 * @param downstream source from where data is loaded
	 * @param upstream destination for loaded data
	 */
	private void saveData( DataSource<Customer> downstream, CrudRepository<Customer, String> upstream ) {
		//
		if( guard( downstream, upstream ) ) {
			//
			switch( status ) {
			//
			case 0:		// initial, ignore
			case 2:		// load mock data, ignore
				break;

			case 1:
				try {
					Iterable<Customer> data = upstream.findAll();
					downstream.saveData( data );

				} catch (IOException e) {
					status = -1; 	// error
					// e.printStackTrace();
				}
				break;

			default:	// error
				System.err.println( this.getClass().getSimpleName() + ": probeData() Error loading data" );
				break;
			}
		}
	}


	/**
	 * Guard function to protect load-/saveData() methods from none-present
	 * downstream DataSource or upstream Respository.
	 * 
	 * @param downstream
	 * @param upstream
	 * @return
	 */
	private boolean guard( DataSource<Customer> downstream, CrudRepository<Customer, String> upstream ) {
		return downstream != null && upstream != null;
	}

	private boolean guard( Object arg, String msg ) {
		if( arg == null ) {
			throw new IllegalArgumentException( "argument: " + msg );
		}
		return true;
	}

}
