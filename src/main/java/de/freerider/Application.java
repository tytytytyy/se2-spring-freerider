package de.freerider;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import de.freerider.model.Customer;
import de.freerider.repository.CustomerRepository;
import de.freerider.repository.*;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		


		Customer hans = new Customer("Peter", "Hans", "hans@web.de");
		Customer marie = new Customer("Ale", "Marie", "marie@web.de");
		Customer baran = new Customer("Acar", "Baran", "baran@web.de");
		Customer tom = new Customer("Jam", "Tom", "tom@web.de");
		Customer tina = new Customer("Tina", "Schwarz", "tina@web.de");
		
		List cList = new ArrayList<Customer>(); 
		CustomerRepository cR = new CustomerRepository();


		tina.setId("001");
		cR.save(tina);

		System.out.println(tina.getFirstName());

		cList.add(hans);
		cList.add(baran);
		cList.add(marie);
		cList.add(tom);
		

		
		cR.saveAll(cList);
		
		cR.save(tina);
		tina.setId("001");

		cR.existsById("001");
		cR.findAll();
		cR.count();
		cR.deleteById("001");
		cR.delete(hans);
		
		System.out.println(cR.count());


		//SpringApplication.run(Application.class, args);

	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);

			}

			

			
			
		};
	}

}