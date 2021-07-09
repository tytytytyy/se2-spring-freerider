package de.freerider.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.freerider.datamodel.Customer;
import de.freerider.datamodel.Customer.Status;


@Component
@Qualifier("CustomerRepository_Impl")
public class CustomerRepository implements CrudRepository<Customer, String> {
	//
	private final IDGenerator idGen = new IDGenerator( "C", IDGenerator.IDTYPE.NUM, 6 );
	Map<String, Customer> customerList;

	
	public CustomerRepository(){
		customerList = new HashMap<String, Customer>();
	}
	
	@Override
	public <S extends Customer> S save(S entity) {
		
		if(entity == null) {
	         throw new IllegalArgumentException("No customer can be saved that is null");
		}
		
		Iterable<Customer> entities = this.findAll();
		
		for (Customer c : entities ) {
		
			if(c.getId() == entity.getId()) {
				
				customerList.replace(entity.getId(), entity);
				entity.setStatus(Status.Active);


				return (S) c;
			}
			
		}
		
		if(Optional.ofNullable(entity.getId()).equals("") || Optional.ofNullable(entity.getId()).isEmpty() || existsById(entity.getId())) {
			
			entity.setStatus(Status.New);

			String newId = idGen.nextId();
			
			while(existsById(newId)) {
				newId = idGen.nextId();
			}
			customerList.put(newId, entity);
			entity.setId(newId);
			
			entity.setStatus(Status.InRegistration);

			
		}else {
			customerList.put(entity.getId(), entity);

		}

		entity.setStatus(Status.Active);
		
		return entity;
	}

	@Override
	public <S extends Customer> Iterable<S> saveAll(Iterable<S> entities) {
       
		if(entities==null) {
	           throw new IllegalArgumentException("cannt be null");
	    }

		for (Customer entity : entities) {
			
			save(entity);
			
			}
		
		return entities;
	}

	@Override
	public Optional<Customer> findById(String id) {
		
		if(id==null) {
			 throw new IllegalArgumentException("cannt be null");
		}
		
		if(id=="") {
			return Optional.empty();
		}
	
		return Optional.ofNullable(customerList.get(id));
	}

	@Override
	public boolean existsById(String id) {
		
		if(id==null) {
			 throw new IllegalArgumentException("cannt be null");
		}
		return customerList.containsKey(id);
	}

	@Override
	public Iterable<Customer> findAll() {
		
		return customerList.values();
	}

	@Override
	public Iterable<Customer> findAllById(Iterable<String> ids) {

		if(ids==null) {
			 throw new IllegalArgumentException("cannt be null");
		}
		List cList = new ArrayList<Customer>(); 
		
		for (String id : ids) {
			
			if(!id.isEmpty()) {
				cList.add(customerList.get(id));
			}
		}
		
		return cList;
	}

	@Override
	public long count() {
		
		return customerList.size();
	}

	@Override
	public void deleteById(String id) {
		
		if(id==null) {
			 throw new IllegalArgumentException("cannt be null");
		}
		
		if(existsById(id)) {
			customerList.remove(id);
		}

	}

	@Override
	public void delete(Customer entity) {
	
		if(entity == null || entity.getId()==(null)) {	
			 throw new IllegalArgumentException("cannt be null");
		 }
		
		
		
		customerList.remove(entity.getId());
		entity.setStatus(Status.Deleted);	
		}

	@Override
	public void deleteAllById(Iterable<? extends String> ids) {
	
		if(ids == null) {	
			 throw new IllegalArgumentException("cannt be null");
		}
		
		for (String id : ids) {
			customerList.get(id).setStatus(Status.Deleted);	
			customerList.remove(id);

		}
	}

	@Override
	public void deleteAll(Iterable<? extends Customer> entities) {
		
		if(entities == null) {	
			 throw new IllegalArgumentException("cannt be null");
		}
		
		for (Customer c : entities) {
			
			if(c.getId() ==null) {
		         throw new IllegalArgumentException("id cannt be null");
			}
			
			this.customerList.remove(c.getId());
			c.setStatus(Status.Deleted);	

		}		
	}

	@Override
	public void deleteAll() {
		
		customerList.clear();		
	}

}

