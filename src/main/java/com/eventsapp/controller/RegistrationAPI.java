package com.eventsapp.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
import com.eventsapp.valueobjects.RegistrationMedium;

@RestController
@RequestMapping("/registrations")
public class RegistrationAPI {
	
	@Autowired
	RegistrationRepository repo;
	
	@GetMapping
	public Iterable<RegistrationMedium> getAll() {
		Iterable<Registration> regs = repo.findAll();
		
		Registration[] regArray = StreamSupport.stream(regs.spliterator(), false).toArray(Registration[]::new);
		RegistrationMedium[] regMedArray = new RegistrationMedium[regArray.length];
		for (int i = 0; i < regArray.length; i++) {
			regMedArray[i] = Registration.convertRegistrationToMedium(regArray[i]);
		}
		
		Iterable<RegistrationMedium> regMedIterable = Arrays.asList(regMedArray);
		return regMedIterable;
	}
	
	// search for registration by both event ID and customer ID
/*	@GetMapping("/{eventId}/{customerId}")
	public Optional<Registration> getRegistrationById(@PathVariable long eventId, @PathVariable long customerId) {
		Optional<Registration> registration = Optional.of(repo.findByEventIdAndCustomerId(eventId, customerId));
		return registration;
	}*/
	
	@GetMapping("/{registrationId}")
	public Optional<RegistrationMedium> getRegistrationById(@PathVariable("registrationId") long id) {
		Optional<Registration> regOptional= repo.findById(id);
		
		if (!regOptional.isPresent()) {
			Optional<RegistrationMedium> regMedOptional = Optional.ofNullable(null);
			return regMedOptional;
		}
		
		Registration registration = regOptional.get();
		
		RegistrationMedium regMed = Registration.convertRegistrationToMedium(registration);
		Optional<RegistrationMedium> regMedOptional = Optional.of(regMed);
		return regMedOptional;
	}
	
	@PostMapping
	public ResponseEntity<?> addRegistration(@RequestBody RegistrationMedium newRegistrationMedium, UriComponentsBuilder uri) {	
		Registration newRegistration = RegistrationMedium.convertMediumToRegistration(newRegistrationMedium);
		Registration existingRegistration = repo.findByEventIdAndCustomerId(newRegistration.getEventId(), newRegistration.getCustomerId());
		
		// Can't add a new registration if one already exists with this event ID and customer ID
		if (existingRegistration != null) {
			return ResponseEntity.badRequest().body("Event registration already exists for this customer.");
		}
		
		// If any of the new registration fields are blank, don't create a new registration
		if (newRegistration.getEventId() == 0 || newRegistration.getCustomerId() == 0 
				|| newRegistration.getRegistrationDate() == null || newRegistration.getNotes() == null
				|| newRegistration.getNotes().equals("")) {
			return ResponseEntity.badRequest().body("Fields cannot be empty.");
		}
		
		newRegistration = repo.save(newRegistration);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/eventId/userId").buildAndExpand(newRegistration.getEventId(), newRegistration.getCustomerId()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		
		return response;
	}
	
/*	@PutMapping("/{eventId}/{customerId}")
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
	}*/
	
	@PutMapping("/{eventId}")
	public ResponseEntity<?> putRegistration(
			@RequestBody RegistrationMedium newRegistrationMedium,
			@PathVariable("eventId") long eventId) 
	{
		Registration newRegistration = RegistrationMedium.convertMediumToRegistration(newRegistrationMedium);
		
		// Make sure we aren't changing the event ID to an existing one
		Registration[] newRegistrations = repo.findByEventId(newRegistration.getEventId());
		if (newRegistrations.length != 0 && newRegistration.getEventId() != eventId) {
			return ResponseEntity.badRequest().body("This event already exists.");
		}
	
		Registration[] registrations = repo.findByEventId(eventId);
		for (int i = 0; i < registrations.length; i++) {
			// Only set fields that have been changed
			if (newRegistration.getEventId() != 0) {
				registrations[i].setEventId(newRegistration.getEventId());
			}
			if (newRegistration.getRegistrationDate() != null) {
				registrations[i].setRegistrationDate(newRegistration.getRegistrationDate());
			}
			if (newRegistration.getNotes() != null && !newRegistration.getNotes().equals("")) {
				registrations[i].setNotes(newRegistration.getNotes());
			}
			
			repo.save(registrations[i]);
		}
		
		return ResponseEntity.ok().build();
	}

	// Delete single registration by event ID and customer ID
	/*
	@Transactional
	@DeleteMapping("/{eventId}/{customerId}")
	public ResponseEntity<?> deleteRegistration(@PathVariable("eventId") long eventId, @PathVariable("customerId") long customerId) {
		long registrationsDeleted = repo.deleteByEventIdAndCustomerId(eventId, customerId);
		
		// if we didn't delete a registration, return a bad request
		if (registrationsDeleted == 0) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok().build();	
	}	*/
	
	@DeleteMapping("/{eventId}")
	public ResponseEntity<?> deleteRegistrationById(@PathVariable("eventId") long eventId) {
		
		Registration[] registrations = repo.findByEventId(eventId);
		
		if (registrations.length == 0) {
			return ResponseEntity.badRequest().body("Registration does not exist.");
		}
		
		for (int i = 0; i < registrations.length; i++) {
			repo.deleteById(registrations[i].getId());
		}
		
		return ResponseEntity.ok().build();
	}	
}
