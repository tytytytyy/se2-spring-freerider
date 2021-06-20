package de.freerider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import de.freerider.datamodel.Customer;
import de.freerider.datasource.DataSource;
import de.freerider.datasource.DataSourceCustomers_JSON;
import de.freerider.repository.CrudRepository;
import de.freerider.repository.CustomerRepository_Proxy;


/**
 * Single @Configuration class to configure application.
 * 
 * @author svgr2
 *
 */

@Configuration
public class AppConfig {

	@Autowired
	private ApplicationContext applicationContext;

	// CustomerRepository_impl reference auto-wired in CustomerRepository_Proxy
//	@Autowired
//	@Qualifier("CustomerRepository_Impl")
//	private CrudRepository<Customer,String> customerRepository_impl;

	@Autowired
	@Qualifier("CustomerRepository_Proxy")
	private CrudRepository<Customer,String> customerRepository_Proxy;


	/**
	 * Bean Factory-method that controls dependency injection for wiring
	 * CrudRepository<Customer,String> references.
	 * 
	 * @return reference for auto-wiring
	 */
	@Bean
	@Primary
	public CrudRepository<Customer,String> getCustomerRepository() {
		//
		AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
		//
		// create JSON DataSource instance
		DataSource<Customer> dataSource = new DataSourceCustomers_JSON();
//		DataSource<Customer> dataSource = new DataSource_Mock<Customer>();	// don't load/write JSON
		//
		factory.autowireBean( dataSource );		// inject dataSource into Spring
		//
		// explicit injection of dataSource into customerRepository_Proxy
		((CustomerRepository_Proxy)customerRepository_Proxy).inject( dataSource );
		//
		CrudRepository<Customer,String> repository = customerRepository_Proxy;
		return repository;
	}

}
