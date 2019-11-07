package com.eventsapp.persistence;

import org.springframework.data.repository.CrudRepository;

import com.eventsapp.valueobjects.Event;

//InMemory Repository of Event
public interface EventRepository extends CrudRepository<Event, Long> {
	
	// Find by name
	Event findByCode(String code);
		
	// Delete
	long deleteByCode(String code);
	
}