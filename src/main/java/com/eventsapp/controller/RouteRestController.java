package com.eventsapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eventsapp.persistence.UserRepository;
import com.eventsapp.valueobjects.User;

@RestController
@RequestMapping("/home")
public class RouteRestController {
	private UserRepository repo = new UserRepository();
	
	@GetMapping(path="/users/{userName}")
	@ResponseBody
	public User getUser(@PathVariable String userName) {
		User currentUser = repo.findByName(userName);
		
		return currentUser;
	}
	
}