package com.eventsapp.persistence;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.eventsapp.valueobjects.Registration;

//InMemory Repository of User
public interface RegistrationRepository extends CrudRepository<Registration, Long> {
	
	// Find by eventId and customerId
	Registration findByEventIdAndCustomerId(long eventId, long customerId);
	
	// Delete by eventId and customerId
	long deleteByEventIdAndCustomerId(long eventId, long customerId);
		
}