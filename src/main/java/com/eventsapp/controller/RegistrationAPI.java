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
	
	@GetMapping("/{eventId}/{customerId}")
	public Optional<Registration> getRegistrationById(@PathVariable long eventId, @PathVariable long customerId) {
		Optional<Registration> registration = Optional.of(repo.findByEventIdAndCustomerId(eventId, customerId));
		return registration;
	}
	
	@PostMapping
	public ResponseEntity<?> addRegistration(@RequestBody Registration newRegistration, UriComponentsBuilder uri) {	
		Registration existingRegistration = repo.findByEventIdAndCustomerId(newRegistration.getEventId(), newRegistration.getCustomerId());
		
		// Can't add a new registration if one already exists with this event ID and customer ID
		if (existingRegistration != null) {
			return ResponseEntity.badRequest().build();
		}
		
		// If any of the new registration fields are blank, don't create a new registration
		if (newRegistration.getEventId() == 0 || newRegistration.getCustomerId() == 0 
				|| newRegistration.getDate() == null || newRegistration.getNotes() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		newRegistration = repo.save(newRegistration);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/eventId/userId").buildAndExpand(newRegistration.getEventId(), newRegistration.getCustomerId()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		
		return response;
	}
	
	@PutMapping("/{eventId}/{customerId}")
	public ResponseEntity<?> putRegistration(@RequestBody Registration newRegistration, 
			@PathVariable("eventId") long eventId, @PathVariable("customerId") long customerId) {
		Registration registration = repo.findByEventIdAndCustomerId(eventId, customerId);
		
		// If the registration doesn't exist, we can't update its info
		if (registration == null) {
			return ResponseEntity.badRequest().build();
		}
		
		// If any of the updated registration fields are blank, don't update the registration
		if (newRegistration.getEventId() == 0 || newRegistration.getCustomerId() == 0 
				|| newRegistration.getDate() == null || newRegistration.getNotes() == null) {
			
		}
		
		registration.setEventId(newRegistration.getEventId());
		registration.setCustomerId(newRegistration.getCustomerId());
		registration.setDate(newRegistration.getDate());
		registration.setNotes(newRegistration.getNotes());
		repo.save(registration);

		return ResponseEntity.ok().build();
	}
	
	@Transactional
	@DeleteMapping("/{eventId}/{customerId}")
	public ResponseEntity<?> deleteRegistration(@PathVariable("eventId") long eventId, @PathVariable("customerId") long customerId) {
		long registrationsDeleted = repo.deleteByEventIdAndCustomerId(eventId, customerId);
		
		// if we didn't delete a registration, return a bad request
		if (registrationsDeleted == 0) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok().build();	
	}
}
