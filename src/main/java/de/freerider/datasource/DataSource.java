package de.freerider.datasource;

import java.io.IOException;


/**
 * Generic interface for DataSource from where collections of entities
 * can be loaded or written to.
 * 
 * @param <T> entity type
 * 
 * @author svgr2
 *
 */
public interface DataSource<T> {

	/**
	 * Functional interface to collect entities from source into A destination
	 * @param <T> entity type
	 */
	@FunctionalInterface
	interface DataCollector<T> {
	    void collect( Iterable<T> destination );
	}


	/**
	 * Save entities.
	 * 
	 * @param entities to save
	 * @throws IOException thrown in case of IO error
	 */
	void saveData( Iterable<T> entities ) throws IOException;


	/**
	 * Load entities.
	 * 
	 * @param destination of entities loaded from data source
	 * @throws IOException thrown in case of IO error
	 */
	void loadData( DataCollector<T> destination ) throws IOException;

}
