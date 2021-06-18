package de.freerider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.freerider.datamodel.Customer;
import de.freerider.datasource.DataSource;
import de.freerider.datasource.DataSourceCustomers_JSON;
import de.freerider.datasource.DataSource_Mock;
import de.freerider.repository.CrudRepository;
import de.freerider.repository.CustomerRepository_Proxy;


@Configuration
public class AppConfig {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	@Qualifier("CustomerRepository_Impl")
	CrudRepository<Customer,String> customerRepository_Impl;

	@Autowired
	@Qualifier("CustomerRepository_Proxy")
	CrudRepository<Customer,String> customerRepository_Proxy;


	/**
	 * Bean Factory method that controls dependency injection for
	 * @Autowired CrudRepository<Customer,String>
	 * 
	 * @return reference to instance for @Autowired CrudRepository<Customer,String> injection
	 */
	@Bean
	public CrudRepository<Customer,String> crudRepository() {
		//
		AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
		//
		// create JSON DataSource instance
		DataSource<Customer> dataSource = new DataSourceCustomers_JSON();
//		DataSource<Customer> dataSource = new DataSource_Mock<Customer>();
		//
		factory.autowireBean( dataSource );		// inject dataSource into Spring
		//
		// inject dataSource into customerRepository_Proxy
		((CustomerRepository_Proxy)customerRepository_Proxy).inject( dataSource );
		//
		return customerRepository_Proxy;
	}

}
