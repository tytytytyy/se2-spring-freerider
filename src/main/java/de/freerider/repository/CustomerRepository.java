package de.freerider.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;


import de.freerider.model.Customer;


@Component
public class CustomerRepository implements CrudRepository<Customer, String> {
	//
	private final IDGenerator idGen = new IDGenerator( "C", IDGenerator.IDTYPE.NUM, 6 );
	Map<String, Customer> customerList;

	
	public CustomerRepository(){
		customerList = new HashMap<String, Customer>();
	}
	
	@Override
	public <S extends Customer> S save(S entity) {
		
		if(Optional.ofNullable(entity.getId()).equals("") || Optional.ofNullable(entity.getId()).isEmpty()) {
			
			String newId = idGen.nextId();
			while(existsById(newId)) {
				newId = idGen.nextId();
			}
			
			entity.setId(newId);
		}

		customerList.put(entity.getId(), entity);
		
		return entity;
	}

	@Override
	public <S extends Customer> Iterable<S> saveAll(Iterable<S> entities) {
       

		for (Customer entity : entities) {
			
			save(entity);
			
			}
		
		return entities;
	}

	@Override
	public Optional<Customer> findById(String id) {
	
		
		return Optional.of(customerList.get(id)); //?
	}

	@Override
	public boolean existsById(String id) {
		
		return customerList.containsKey(id);
	}

	@Override
	public Iterable<Customer> findAll() {
		
		return customerList.values();
	}

	@Override
	public Iterable<Customer> findAllById(Iterable<String> ids) {

		List cList = new ArrayList<Customer>(); 
		
		for (String id : ids) {
			cList.add(customerList.get(id));
		}
		
		return cList;
	}

	@Override
	public long count() {
		
		return customerList.size();
	}

	@Override
	public void deleteById(String id) {
		
		if(existsById(id)) {
			customerList.remove(id);

		}

	}

	@Override
	public void delete(Customer entity) {
		
		customerList.remove(entity.getId());
	}

	@Override
	public void deleteAllById(Iterable<? extends String> ids) {
		
		for (String id : ids) {
			customerList.remove(id);
		}
	}

	@Override
	public void deleteAll(Iterable<? extends Customer> entities) {
		
		for (Customer c : entities) {
			this.customerList.remove(c.getId());
		}		
	}

	@Override
	public void deleteAll() {
		
		customerList.clear();		
	}

}
