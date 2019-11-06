package com.eventsapp.persistence;

import org.springframework.data.repository.CrudRepository;

import com.eventsapp.valueobjects.User;

//InMemory Repository of User
public interface UserRepository extends CrudRepository<User, Long> {
	
	// Find by name
	User findByName(String name);
		
	// Delete
	Boolean deleteByName(String name);
	
	// Update
//	void updateByName(String name);
	
}