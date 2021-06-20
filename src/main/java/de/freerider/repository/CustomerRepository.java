package de.freerider.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.freerider.datamodel.Customer;


/**
 * Customer-Repository implementation of the CrudRepository<Customer, String>
 * interface using an internal HashMap<String, Customer>.
 * 
 * @Component with @Qualifier to differentiate from other components of the
 * CrudRepository<Customer, String> interface.
 * 
 */

/*
 * REPLACE CustomerRepository.java with your implementation.
 * //
 * KEEP @Qualifier("CustomerRepository_Impl") annotation.
 */

@Component
@Qualifier("CustomerRepository_Impl")
class CustomerRepository implements CrudRepository<Customer, String> {
	//
	private final IDGenerator idGen = new IDGenerator( "C", IDGenerator.IDTYPE.NUM, 6 );


	@Override
	public <S extends Customer> S save( S entity ) {
		// TODO Auto-generated method stub
		return entity;
	}

	@Override
	public <S extends Customer> Iterable<S> saveAll( Iterable<S> entities ) {
		// TODO Auto-generated method stub
		Iterable<S> result = List.of();		// return empty, immutable list
		return result;
	}

	@Override
	public Optional<Customer> findById( String id ) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsById( String id ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Customer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Customer> findAllById( Iterable<String> ids ) {
		// TODO Auto-generated method stub
		Iterable<Customer> result = List.of();		// return empty, immutable list
		return result;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById( String id ) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete( Customer entity ) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteAllById( Iterable<? extends String> ids ) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteAll( Iterable<? extends Customer> entities ) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
	}

}
