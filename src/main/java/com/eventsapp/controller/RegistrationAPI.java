package com.eventsapp.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.eventsapp.persistence.RegistrationRepository;
import com.eventsapp.valueobjects.Registration;

@RestController
@RequestMapping("/registrations")
public class RegistrationAPI {
	
	@Autowired
	RegistrationRepository repo;
	
	@GetMapping
	public Iterable<Registration> getAll() {
		return repo.findAll();
	}
	
	@GetMapping("/{eventId}/{userId}")
	public Registration getRegistrationById(@PathVariable long eventId, @PathVariable long userId) {
		Registration[] registrations = repo.findByEventId(eventId);
		System.out.println(registrations[0]);
		System.out.println(registrations[1]);
		System.out.println(registrations[2]);
		return registrations[0];
	}
	/*
	@PostMapping
	public ResponseEntity<?> addRegistration(@RequestBody Registration newRegistration, UriComponentsBuilder uri) {		
		if (newRegistration.getEventId() == 0 || newRegistration.getEventId() == 0 
				|| newRegistration.getDate() == null || newRegistration.getNotes() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		newRegistration = repo.save(newRegistration);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/eventId").buildAndExpand(newRegistration.getEventId()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		
		return response;
	}
	
	@PutMapping("/{userName}")
	public ResponseEntity<?> putUser(@RequestBody User newUser, @PathVariable("userName") String userName) {
		User user = repo.findByName(userName);
		
		if (user == null || newUser.getName() == null || newUser.getPassword() == null || newUser.getEmail() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		user.setName(newUser.getName());
		user.setPassword(newUser.getPassword());
		user.setEmail(newUser.getEmail());
		repo.save(user);

		return ResponseEntity.ok().build();
	}
	
	@Transactional
	@DeleteMapping("/{userName}")
	public ResponseEntity<?> deleteUser(@PathVariable("userName") String userName) {
		long usersDeleted = repo.deleteByName(userName);
		if (usersDeleted == 0) {
			return ResponseEntity.badRequest().build();
		}
		//assertThat(usersDeleted).isEqualTo(1);
		
		return ResponseEntity.ok().build();	
	}*/
}
