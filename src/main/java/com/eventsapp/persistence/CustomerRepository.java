package com.eventsapp.persistence;

import org.springframework.data.repository.CrudRepository;

import com.eventsapp.valueobjects.Customer;

//InMemory Repository of User
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
	// Find by name
	Customer findByName(String name);
		
	// Delete
	long deleteByName(String name);
	
}