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

import com.eventsapp.persistence.EventRepository;
import com.eventsapp.valueobjects.Event;

@RestController
@RequestMapping("/events")
public class EventAPI {
	
	@Autowired
	EventRepository repo;
	
	@GetMapping
	public Iterable<Event> getAll() {
		return repo.findAll();
	}
	
	@GetMapping("/{eventCode}")
	public Optional<Event> getEventByCode(@PathVariable String eventCode) {
		Optional<Event> optionalEvent = Optional.ofNullable(repo.findByCode(eventCode));
		return optionalEvent;
	}
	
	@PostMapping
	public ResponseEntity<?> addEvent(@RequestBody Event newEvent, UriComponentsBuilder uri) {
		Event existingEvent = repo.findByCode(newEvent.getCode());
		
		// Can't add a new event if one already exists with this code
		if (existingEvent != null) {
			return ResponseEntity.badRequest().body("Event code already exists.");
		}
		
		// If any of the new event fields are blank, don't create a new event
		if (newEvent.getCode() == null || newEvent.getTitle() == null || newEvent.getDescription() == null
				|| newEvent.getCode().equals("") || newEvent.getTitle().equals("") || newEvent.getDescription().equals("")) {
			return ResponseEntity.badRequest().body("Fields cannot be empty.");
		}
		
		newEvent = repo.save(newEvent);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/eventCode").buildAndExpand(newEvent.getCode()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		
		return response;
	}
	
	@PutMapping("/{eventCode}")
	public ResponseEntity<?> putEvent(@RequestBody Event newEvent, @PathVariable("eventCode") String eventCode) {
		Event event = repo.findByCode(eventCode);
		Event newEventTest = repo.findByCode(newEvent.getCode());
		
		// If the event doesn't exist, we can't update its info
		if (event == null) {
			return ResponseEntity.badRequest().body("Event does not exist.");
		}
		
		// If the updated event code already exists, can't update since the event code needs to be unique
		if (newEventTest != null) {
			return ResponseEntity.badRequest().body("Event code already exists.");
		}
				
		// If any of the updated event fields are blank, don't update the event
		if (newEvent.getTitle() == null || newEvent.getDescription() == null
				|| newEvent.getTitle().equals("") || newEvent.getDescription().equals("")) {
			return ResponseEntity.badRequest().body("Fields cannot be empty.");
		}
		
		event.setDescription(newEvent.getDescription());
		event.setTitle(newEvent.getTitle());
		repo.save(event);

		return ResponseEntity.ok().build();
	}
	
	@Transactional
	@DeleteMapping("/{eventCode}")
	public ResponseEntity<?> deleteUser(@PathVariable("eventCode") String eventCode) {
		long eventsDeleted = repo.deleteByCode(eventCode);
		
		// if we didn't delete an event, return a bad request
		if (eventsDeleted == 0) {
			return ResponseEntity.badRequest().body("Event does not exist.");
		}
		
		return ResponseEntity.ok().build();	
	}
}
