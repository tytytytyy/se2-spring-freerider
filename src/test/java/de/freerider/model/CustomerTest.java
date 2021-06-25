package de.freerider.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import de.freerider.model.Customer;
import de.freerider.model.Customer.Status;
import de.freerider.repository.CustomerRepository;

@SpringBootTest
class CustomerTest {
	
	Customer mats = new Customer("Peter", "Hans", "hans@web.de");
	Customer thomas= new Customer("Ale", "Marie", "marie@web.de");
	
	private CustomerRepository cR = new CustomerRepository();

	@Test
	void contextLoads() {
	}
	
	/*Id Tests*/
	@Test
	public void testIdNull() {
	

		assertNull(mats.getId());
		assertNull(thomas.getId());
		
		cR.save(mats);
		cR.save(thomas);	

	}
	
	@Test
	public void testSetId() {
		mats.setId("1");
		assertNotNull(mats.getId());
	}
	
	@Test
	public void testSetIdOnlyOnce(){
		
		mats.setId("1");
		thomas.setId("1");
		
		cR.save(mats);
		cR.save(thomas);

		assertTrue(cR.count()==1);
	}
	
	@Test
	public void testResetId(){
		
		mats.setId(null);
		assertNull(mats.getId());
	}
	
	
	/*Name Tests*/

	@Test
	public void testNamesInitial() {
		
		mats.setFirstName("");
		mats.setLastName("");
		
		assertNotNull(mats.getFirstName());
		assertNotNull(mats.getLastName());
	}
	
	@Test
	public void testNamesSetNull() {
		
		mats.setFirstName(null);
		mats.setLastName(null);

		assertNull(mats.getFirstName());
		assertNull(mats.getLastName());
	}
	
	@Test
	public void testSetNames() {
		
		mats.setFirstName("Mats");
		mats.setLastName("Schmidt");
		
		assertTrue(mats.getFirstName().equals("Mats"));
		assertTrue(mats.getLastName().equals("Schmidt"));
	}	
	
	/*Contacts Tests*/

	@Test
	public void testContactsInitial() {
		
		mats.setContact("");
		assertNotNull(mats.getContact());
		
	}
	
	@Test
	public void testContactsSetNull() {
		
		mats.setContact(null);
		assertNull(mats.getContact());
	}
	
	@Test
	public void testSetContacts() {
		
		mats.setContact("Berlin");
		
		assertTrue(mats.getContact().equals("Berlin"));
	}	
	
	/*Contacts Tests*/

	@Test
	public void testStatusInitial() {
		
		assertEquals(mats.getStatus(), Customer.Status.New);
	}
	
	@Test
	public void testSetStatus() {
		
		mats.setStatus(Customer.Status.InRegistration);
		assertEquals(mats.getStatus(), Customer.Status.InRegistration);
		
		mats.setStatus(Customer.Status.Active);
		assertEquals(mats.getStatus(), Customer.Status.Active);
		
		mats.setStatus(Customer.Status.Suspended);
		assertEquals(mats.getStatus(), Customer.Status.Suspended);
		
		mats.setStatus(Customer.Status.Deleted);
		assertEquals(mats.getStatus(), Customer.Status.Deleted);
		
		
	}
	
	
	
}
