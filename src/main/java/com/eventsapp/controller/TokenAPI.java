package com.eventsapp.controller;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventsapp.persistence.CustomerRepository;
import com.eventsapp.security.Authenticator;
import com.eventsapp.security.JWTHelper;
import com.eventsapp.valueobjects.Customer;
import com.eventsapp.valueobjects.Token;
import com.eventsapp.valueobjects.TokenRequestData;

@RestController
@RequestMapping("/token")
public class TokenAPI {
	@Autowired
	CustomerRepository repo;
	
	JWTHelper jwtHelper = new JWTHelper();
	
	@PostMapping(consumes = "application/json")
	public ResponseEntity<?> getToken(@RequestBody TokenRequestData tokenRequestData) {
		
		String username = tokenRequestData.getUsername();
		String password = tokenRequestData.getPassword();
		String scopes = tokenRequestData.getScopes();
		
		Stream<Customer> customerStream = StreamSupport.stream(repo.findAll().spliterator(), false);
		
		if (username != null && username.length() > 0 
				&& password != null && password.length() > 0 
				&& Authenticator.checkPassword(username, password, customerStream)) {
			Token token = jwtHelper.createToken(scopes);
			ResponseEntity<?> response = ResponseEntity.ok(token);
			return response;			
		}
		
		// bad request
		return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
	}
	
	
}
