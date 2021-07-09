package de.freerider.datamodel;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import de.freerider.datamodel.Customer.Status;


/**
 * Factory for objects from model package
 * 
 * @author svgr2
 */

@Component
public class DataFactory {


	/**
	 * Factory method to create Customer object from key-value pairs stored in map.
	 * 
	 * @param map key-value pairs of attribute names (key) and value
	 * 
	 * @return customer object with attributes initialized from values[]
	 * @throws IllegalArgumentException thrown for illegal values
	 */
	public Customer createCustomer( HashMap<String,Object> map ) throws IllegalArgumentException {
		if( map == null )
			throw new IllegalArgumentException( "argument: map is null" );
		//
		String id = (String)map.get( "id" );
		if( id != null && id.equals( "null" ) ) {
			id = null;
		}
		String firstName = (String)map.get( "firstName" );
		String lastName = (String)map.get( "lastName" );
		String contact = (String)map.get( "contact" );
		String statusStr = (String)map.get( "status" );

		// sanity checks
		if( id == null || id.length() == 0 )	// error, not tolerated
			throw new IllegalArgumentException( "id null or empty (or field not found)" );
		//
		if( statusStr == null )		// error, not tolerated
			throw new IllegalArgumentException( "status null (or field not found)" );
		//
		//if( firstName == null || lastName == null || contact == null ) {
			// warning that fields were not found
			// throw new UnknownFormatConversionException( "warning: fields lastName, firstName, contact null" );
		//}
		//
		Status status = Status.valueOf( statusStr );	// throws IllegalArgumentException, NullpointerException
		Customer customer = new Customer( lastName, firstName, contact );
		customer.setId( id );
		customer.setStatus( status );
		//
		return customer;
	}

	/**
	 * Create some Customer mock data.
	 * 
	 * @return Customer mock data
	 */
	public Iterable<Customer> createCustomerMockData() {
		final Customer c1 = new Customer( "Meyer", "Anne", "ma2958@gmx.de" );
		final Customer c2 = new Customer( "Korn", "Hellen", "hellen@gmail.de" );
		final Customer c3 = new Customer( "May", "Angelika", "s482045@berliner-hochschule.de" );
		final Customer c4 = new Customer( "Baer", "Max", "max3245@gmx.de" );
		final Customer c5 = new Customer( "Vogel", "Paul", "paul.vogel@erliner-hochschule.de" );
		//
		c4.setStatus( Status.InRegistration );
		//
		Iterable<Customer> customers = List.of( c1, c2, c3, c4, c5 );
		return customers;
	}

}
