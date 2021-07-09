package de.freerider.datasource;

import java.io.IOException;


/**
 * Mock data source that implements DataSource interface without effect.
 * 
 * @author svgr2
 *
 */
public class DataSource_Mock<T> implements DataSource<T> {

	@Override
	public void saveData( Iterable<T> entities ) throws IOException {
		// TODO Auto-generated method stub that does nothing
		System.err.println( this.getClass().getSimpleName() + ".saveData(): nothing saved." );
	}

	@Override
	public void loadData( DataCollector<T> destination ) throws IOException {
		// TODO Auto-generated method stub that does nothing
		System.err.println( this.getClass().getSimpleName() + ".loadData(): nothing loaded." );
	}

}
