package com.eventsapp.security;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.eventsapp.valueobjects.Customer;

public class Authenticator {

	public static boolean checkPassword(String username, String password, Stream<Customer> customerStream) {
		
		// Check the stream to see if the entered user name exists
		List<Customer> selectedCustomer = customerStream.filter(x->x.getName().contains(username)).collect(Collectors.toList());
				
		// If we don't find a user, fail authentication
		if (selectedCustomer.size() != 1) {
			return false;
		}
		
		// Check to see if the stored user's password equals the entered password
		Customer customer = selectedCustomer.get(0);
		if (customer.getPassword().equals(password)) {
			return true;
		}
		
		return false;
	}
	
}
