package com.javawiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.javawiz.entity.Customer;
import com.javawiz.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class AccessingDataMongodbApplication implements CommandLineRunner {

	@Autowired
	private CustomerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataMongodbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		// save a couple of customers
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		log.debug("Customers found with findAll():");
		log.debug("-------------------------------");
		repository.findAll().forEach(customer -> log.debug("{}", customer));
		
		// fetch an individual customer
		log.debug("Customer found with findByFirstName('Alice'):");
		log.debug("--------------------------------");
		log.debug("{}", repository.findByFirstName("Alice"));

		log.debug("Customers found with findByLastName('Smith'):");
		log.debug("--------------------------------");
		repository.findByLastName("Smith").forEach(customer -> log.debug("{}", customer));
	}
}
