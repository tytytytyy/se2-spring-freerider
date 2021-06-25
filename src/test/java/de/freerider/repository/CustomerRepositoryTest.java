package de.freerider.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import de.freerider.model.Customer;
import de.freerider.repository.CrudRepository;

import org.junit.jupiter.api.Test;


@SpringBootTest
public class CustomerRepositoryTest {
	
	@Autowired
	CrudRepository<Customer,String> cR;
	
	// two sample customers
	Customer mats = new Customer("Peter", "Hans", "hans@web.de");
	Customer thomas= new Customer("Ale", "Marie", "marie@web.de");
	
	
	List<Customer> customers = new ArrayList<Customer>();
	List<String> ids = new ArrayList<String>();
	

	@BeforeEach
	public void setUpEach() {
		cR.deleteAll();
	}



	@Test
	void contextLoads() {
	}
	
	/*Konstruktortest*/
	
	@Test
	public void testConstructor() {
		
		cR = new CustomerRepository();
		assertTrue(cR.count() == 0);
		
	}
	
	/*Save Tests*/
	//Regulaere Faelle
	
	@Test
	public void testSaveCustomers() {
		cR.save(mats);
		cR.save(thomas);
		assertEquals(cR.count(),2);
	}
	
	@Test
	public void testSaveCustomer() {
		
		cR.save(mats);
		assertTrue(cR.count() == 1);
	}
	
	@Test
	public void testSaveIdNullCustomer() {
		
		mats.setId(null);
		cR.save(mats);
		assertTrue(cR.count() == 1);
	}
	
	
	//Sonderfaelle
	
	@Test
	public void testSetIdOnlyOnce(){

		mats.setId("1");
		thomas.setId("1");

		cR.save(mats);
		cR.save(thomas);

		assertEquals(cR.count(),1);
		assertNotEquals(cR.findById("1"),Optional.of(mats));
		assertEquals(cR.findById("1"),Optional.of(thomas));
	}
	
	
	/*SaveAll Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testSaveAllCustomers() {
		
		customers.add(mats);
		customers.add(thomas);

		cR.saveAll(customers);

		assertEquals(cR.count(), 2);
		assertNotNull(cR.findAll());

	}
	
	@Test
	public void testSaveAllIdNullCustomers() {
	
		customers.add(mats);
		customers.add(thomas);

		cR.saveAll(customers);

		assertEquals(cR.count(), 2);

	}
	
	//Sonderfaelle
	
	@Test
	public void testSaveAllNull(){
		
		assertThrows(IllegalArgumentException.class, () -> cR.saveAll(null),
		           "Obj cannt be null");		
				
		assertTrue(cR.count()==0);
	}
	
	
	@Test
	public void testsaveAllSameCustomersId(){
	
		mats.setId("1");
		thomas.setId("1");

		customers.add(mats);
		customers.add(thomas);
		
		cR.saveAll(customers);
		
		assertEquals(cR.count(), 1);

		assertNotEquals(cR.findById("1"),Optional.of(mats));
		assertEquals(cR.findById("1"), Optional.of(thomas));

	}
	
	
	/*FindbyId Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testFindbyId() {
		
		mats.setId("1");
		cR.save(mats);

		assertEquals(cR.findById("1"), Optional.of(mats));

	}
	
	@Test
	public void testFindbyIdNull() { //?
		
		mats.setId(null);
		cR.save(mats);

		assertThrows(IllegalArgumentException.class, () -> cR.existsById(null),
		           "Obj cannt be null");

	}
	
	//Sonderfaelle
	
	@Test
	public void testFindbyIdSameTwice() {
		
		mats.setId("1");
		thomas.setId("1");

		cR.save(mats);
		cR.save(thomas);

		assertNotEquals(cR.findById("1"),Optional.of(mats));
		assertEquals(cR.findById("1"), Optional.of(thomas));

	}
	
	/*FindAll Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testFindAll() {

		cR.save(mats);
		cR.save(thomas);
		
		int i = 0;

	for (Customer entity : cR.findAll()) {
			
			assertNotNull(entity);
			i++;
			
			}
	
	assertEquals(i,2);
	assertEquals(2,cR.count());

	}
	
	public void testFindAllSameId() {
		
		mats.setId("1");
		thomas.setId("1");
		
		cR.save(mats);
		cR.save(thomas);

		int i = 0;

	for (Customer entity : cR.findAll()) {
			
			assertNotNull(entity);
			i++;
			
			}
	assertEquals(i,1);
	assertEquals(i,cR.count());

	}
	
	//Sonderfaelle

	@Test
	public void testFindAllEmpty() {

	for (Customer entity : cR.findAll()) {
			
			assertNotNull(entity);
			
			}
	
	assertEquals(0,cR.count());

	}
	
	
	@Test
	public void testFindAllSomeNull() {

		cR.save(mats);
		cR.save(thomas);
		
		assertThrows(IllegalArgumentException.class, () -> cR.save(null),
		           "Obj cannt be null");		
				
		for (Customer entity : cR.findAll()) {
			
			assertNotNull(entity);
			
			}
		
		assertEquals(2 ,cR.count());

	}
	
	/*FindAllbyIds Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testFindAllById() {

		mats.setId("1");
		thomas.setId("2");
		
		ids.add(mats.getId());
		ids.add(thomas.getId());
		
		cR.save(mats);
		cR.save(thomas);

	
		int i = 0;

	for (Customer entity : cR.findAllById(ids)) {
			
			assertNotNull(entity);
			i++;
			
			}
	assertEquals(i,2);
	assertEquals(i,cR.count());

	}
	
	/*Count Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testCount() {

		mats.setId("1");
		thomas.setId("2");
	
		
		cR.save(mats);
		cR.save(thomas);

	assertEquals(cR.count(),2);

	}
	
	@Test
	public void testCountNull() {

		mats.setId("1");
		thomas.setId(null);
	
		
		cR.save(mats);
		cR.save(thomas);

	assertEquals(cR.count(),2);

	}
	
	@Test
	public void testCountSaveTwice() { 
		
		mats.setId("1");	
		
		cR.save(mats);

		cR.save(mats);
		

		assertEquals(cR.count(),1);

	}
	
	/*Delete Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testdelete() {
		
		thomas.setId("2");
		
		cR.save(thomas);
		cR.delete(thomas);
		
		assertEquals(cR.count(),0);
		assertTrue(!cR.existsById("2"));

	}
	

	
	@Test
	public void testDeleteSameTwice() {
		
		mats.setId("1");
		thomas.setId("2");
		
		cR.save(mats);
		cR.save(thomas);
		
		cR.delete(thomas);
		cR.delete(thomas);

		assertTrue(!(cR.existsById("2")));
		assertEquals(cR.count(),1);

	}
	
	@Test
	public void testDeleteNull() {

		mats.setId("1");
		thomas.setId("2");
		
		cR.save(mats);
		cR.save(thomas);
		
		
		assertThrows(IllegalArgumentException.class, () ->cR.delete(null),
		           "Obj cannt be null");	

		assertEquals(cR.count(),2);
		assertNotNull(cR.findById("2"));

	}
	
	/*DeleteById Tests*/
	//Regulaere Faelle
	
	@Test
	public void testDeleteById() {

		mats.setId("1");
		thomas.setId("2");
	
		
		cR.save(mats);
		cR.save(thomas);
		

		cR.deleteById("1");

		assertEquals(cR.count(),1);
		
		assertThrows(NullPointerException.class, () ->assertNull(cR.findById("1")),
		           "Obj cannt be null");	

	}
	
	
	/*DeleteAll Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testDeleteAll() {

		cR.save(mats);
		cR.save(thomas);
		
		cR.deleteAll();

		assertEquals(cR.count(),0);

	}
	

	
	/*DeleteAllByListTests*/
	//Regulaere Faelle
	
	
	@Test
	public void testDeleteAllByList() {
		
		mats.setId("1");
		thomas.setId("2");
	
		customers.add(mats);
		customers.add(thomas);

		cR.save(mats);
		cR.save(thomas);
		
		cR.deleteAll(customers);
		
		assertTrue(!cR.existsById("1"));
		assertTrue(!cR.existsById("2"));

		assertEquals(cR.count(),0);

	}
}
