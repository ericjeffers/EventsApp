package com.eventsapp.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

import com.eventsapp.persistence.CustomerRepository;
import com.eventsapp.valueobjects.Customer;

@RestController
@RequestMapping("/customers")
public class CustomerAPI {
	
	@Autowired
	CustomerRepository repo;
	
	@GetMapping
	public Iterable<Customer> getAll() {
		return repo.findAll();
	}
	
	@GetMapping("/{customerName}")
	public Optional<Customer> getCustomerByName(@PathVariable String customerName) {
		Optional<Customer> optionalCustomer = Optional.ofNullable(repo.findByName(customerName));
		return optionalCustomer;
	}
	
	@PostMapping
	public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer, UriComponentsBuilder uri) {
		Customer existingCustomer = repo.findByName(newCustomer.getName());
		
		// Can't add a new customer if one already exists with this name
		if (existingCustomer != null) {
			return ResponseEntity.badRequest().body("Customer name already exists.");
		}
		
		// If any of the new customer fields are blank, don't create a new customer
		if (newCustomer.getName() == null || newCustomer.getPassword() == null || newCustomer.getEmail() == null
				|| newCustomer.getName().equals("") || newCustomer.getPassword().equals("") || newCustomer.getEmail().equals("")) {
			return ResponseEntity.badRequest().body("Fields cannot be empty.");
		}
		
		newCustomer = repo.save(newCustomer);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/customerName").buildAndExpand(newCustomer.getName()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		
		return response;
	}
	
	//lookupCustomerByName GET
	@GetMapping("/byname/{username}")
	public ResponseEntity<?> lookupCustomerByNameGet(@PathVariable("username") String username,
			UriComponentsBuilder uri) {
		//  Workshop:  Write an implemenatation to look up a customer by name.  Think about what
		//  your response should be if no customer matches the name the caller is searching for.
		//  With the data model implemented in CustomersRepository, do you need to handle more than
		//  one match per request?
		
		Stream<Customer> customerStream = StreamSupport.stream(repo.findAll().spliterator(), false);
		List<Customer> selectedCustomer = customerStream.filter(x->x.getName().contains(username)).collect(Collectors.toList());
		
		if (selectedCustomer.size() != 1) {
			return ResponseEntity.badRequest().body("User does not exist.");
		}
		
		Customer customer = selectedCustomer.get(0);	
		ResponseEntity<?> response = ResponseEntity.ok(customer);
		
		return response;
	}
	
	@PutMapping("/{customerName}")
	public ResponseEntity<?> putcustomer(@RequestBody Customer newCustomer, @PathVariable("customerName") String customerName) {
		Customer customer = repo.findByName(customerName);
		
		// If the customer doesn't exist, we can't update their info
		if (customer == null) {
			return ResponseEntity.badRequest().body("User does not exist.");
		}
		
		// If any of the updated customer fields are blank, don't update the customer
		if (newCustomer.getPassword() == null || newCustomer.getEmail() == null
				|| newCustomer.getPassword().equals("") || newCustomer.getEmail().equals("")) {
			return ResponseEntity.badRequest().body("Fields cannot be empty.");
		}
		
		customer.setPassword(newCustomer.getPassword());
		customer.setEmail(newCustomer.getEmail());
		repo.save(customer);

		return ResponseEntity.ok().build();
	}
	
	@Transactional
	@DeleteMapping("/{customerName}")
	public ResponseEntity<?> deletecustomer(@PathVariable("customerName") String customerName) {
		long customersDeleted = repo.deleteByName(customerName);
		
		// if we didn't delete a user, return a bad request
		if (customersDeleted == 0) {
			return ResponseEntity.badRequest().body("User does not exist.");
		}
		
		return ResponseEntity.ok().build();	
	}
}
