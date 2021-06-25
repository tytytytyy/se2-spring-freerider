package de.freerider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.freerider.model.Customer;
import de.freerider.model.Customer.Status;
import de.freerider.repository.CrudRepository;
import de.freerider.repository.CustomerRepository;

@SpringBootTest
public class CustomerRepositoryTest {
	
	@Autowired
	CrudRepository<Customer,String> cR;
	
	// two sample customers
	Customer mats = new Customer("Peter", "Hans", "hans@web.de");
	Customer thomas= new Customer("Ale", "Marie", "marie@web.de");
	List<Customer> customers = new ArrayList<Customer>();
	List<String> ids = new ArrayList<String>();
	
	
	private static int loglevel = 2;		// 0: silent; 1: @Test methods; 2: all methods

	@BeforeEach
	public void setUpEach() {
		cR.deleteAll();

		log( "@BeforeEach", "setUpEach()" );
	}

	private static void log( String label, String meth ) {
		if( loglevel >= 2 ) {
			if( label.equals( "@BeforeEach" ) ) {
				System.out.println();
			}
			java.io.PrintStream out_ = System.out;
			if( label.equals( "@BeforeAll" ) || label.equals( "@AfterAll" ) ) {
				System.out.println();
				out_ = System.err;	// print in red color
			}
			out_.println( label + ": " + SampleTests.class.getSimpleName() + "." + meth );
		}
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
		
		cR.deleteAll();
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

		assertEquals(cR.count(),0);
	}
	
	@Test
	public void saveCustomerWithIdNull(){
		
		mats.setId(null);
		cR.save(mats);
		assertEquals(cR.count(),1);
	}
	
	@Test
	public void saveSameCustomer(){
		
		cR.save(mats);
		cR.save(mats);

		assertTrue(cR.count() == 1);
	}
	
	@Test
	public void saveSameCustomerId(){
		
		mats.setId("1");
		thomas.setId("1");

		cR.save(mats);
		cR.save(thomas);

		assertTrue(cR.count() == 1);
	}
	
	/*SaveAll Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testSaveAllCustomers() {
		
		customers.add(mats);
		customers.add(thomas);

		cR.saveAll(customers);

		assertEquals(cR.count(), 2);
		assertNull(cR.findAll());

	}
	
	@Test
	public void testSaveAllIdNullCustomerd() {
		
		mats.setId(null);
		thomas.setId(null);

		customers.add(mats);
		customers.add(thomas);

		cR.saveAll(customers);

		assertEquals(cR.count(), 2);
		assertTrue(cR.findById("1").equals("1"));

	}
	
	//Sonderfaelle
	
	@Test
	public void testSaveAllNull(){

		cR.saveAll(null);
		assertTrue(cR.count()==0);
	}
	
	@Test
	public void testsaveSameCustomers(){
		
		cR.save(mats);
		cR.save(mats);

		assertTrue(cR.count() == 1);
	}
	
	@Test
	public void testsaveSameCustomersId(){
	
		mats.setId("1");
		thomas.setId("1");

		customers.add(mats);
		customers.add(thomas);

		cR.saveAll(customers);
		assertEquals(cR.count(), 1);

	}
	
	
	/*FindbyId Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testFindbyId() {
		
		mats.setId("1");
		cR.save(mats);

		assertEquals(cR.findById("1"),"1");

	}
	
	@Test
	public void testFindbyIdNull() {
		
		mats.setId(null);
		cR.save(mats);

		assertTrue(!cR.existsById(null));

	}
	
	//Sonderfaelle
	
	@Test
	public void testFindbyIdSameTwice() {
		
		mats.setId("1");
		thomas.setId("1");

		cR.save(mats);
		cR.save(thomas);

		assertEquals(cR.findById("1"),mats);
		assertNotEquals(cR.findById("1"),thomas);

	}
	
	/*FindbyId Tests*/
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
	assertEquals(i,cR.count());

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
	public void testFindAllNull() {

		cR.save(null);
		cR.save(null);
		
		int i = 0;

	for (Customer entity : cR.findAll()) {
			
			assertNotNull(entity);
			i++;
			
			}
	assertEquals(i,0);
	assertEquals(i,cR.count());

	}
	
	
	@Test
	public void testFindAllSomeNull() {

		cR.save(mats);
		cR.save(null);
		
		int i = 0;

	for (Customer entity : cR.findAll()) {
			
			assertNotNull(entity);
			i++;
			
			}
	assertEquals(i,1);
	assertEquals(i,cR.count());

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
	public void testCountMultiple() {

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
		
		cR.save(mats);
		cR.save(thomas);
		
		cR.delete(thomas);

		assertEquals(cR.count(),1);
		assertTrue(!cR.existsById("2"));

	}
	

	
	@Test
	public void testDeleteTwice() {

		cR.deleteAll();
		
		mats.setId("1");
		thomas.setId("2");
		
		cR.save(mats);
		cR.save(thomas);
		
		cR.delete(thomas);
		cR.delete(thomas);

		assertTrue(!(cR.existsById("2")));
		
		System.out.println(cR.count());
		assertEquals(cR.count(),1);

	}
	
	@Test
	public void testDeleteNull() {

		mats.setId("1");
		thomas.setId("2");
	
		
		cR.save(mats);
		cR.save(thomas);
		
		cR.delete(null);


	assertEquals(cR.count(),2);
	assertNotNull(cR.findById("2"));

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
	
	
	/*DeleteAllByid Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testDeleteAllById() {

		mats.setId("1");
		thomas.setId("2");
	
		
		cR.deleteAllById(ids);

	assertEquals(cR.count(),0);
	assertNull(cR.findById("2"));
	assertNull(cR.findAll());

	}
	
	/*DeleteAllByid Tests*/
	//Regulaere Faelle
	
	
	@Test
	public void testDeleteAllBySet() {
		
		mats.setId("1");
		thomas.setId("2");
	
		customers.add(mats);
		customers.add(thomas);

		cR.save(mats);
		cR.save(thomas);
		
		cR.deleteAll(customers);
		
		assertEquals(cR.count(),0);
		assertTrue(!cR.existsById("2"));

	}
}
