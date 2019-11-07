package com.eventsapp.persistence;

import org.springframework.data.repository.CrudRepository;

import com.eventsapp.valueobjects.Registration;

//InMemory Repository of User
public interface RegistrationRepository extends CrudRepository<Registration, Long> {
	
	// Find by eventId
	Registration[] findByEventId(long eventId);
		
}