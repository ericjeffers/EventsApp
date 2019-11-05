package com.eventsapp.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.eventsapp.valueobjects.User;
//InMemory Repository of User
public class UserRepository {

	List<User> userRepository = new ArrayList<>();
	
	public UserRepository() {
	 // TODO Auto-generated constructor stub
	 this.initializeSeedData();
	 System.out.println("Initialized Inmemory repostory of Users");
	}
	//Some seed data in memory
	public void initializeSeedData() {
	 User user1 = new User("user1", "password1", "email1");
	 User user2 = new User("user2", "password2", "email2");
	 this.userRepository.add(user1);
	 this.userRepository.add(user2);
	}
	
	@Override
	public String toString() {
	 return "UserRepo [" + userRepository + "]";
	}
	
	public List<User> getUserRepository() {
	 return userRepository;
	}
	
	public void setUserRepository(List<User> userRepository) {
	 this.userRepository = userRepository;
	}
	
	//add a new instance of AccountInfo to the repository
	public void addUser(User user) {
	 this.userRepository.add(user);
	 System.out.println("Added " + user + " to inMemory repository");
	}
	
	//retrieve an AccountInfo from the repository by name
	public User findByName(String userName) {
	 Optional<User> userOptional = this.userRepository.stream()
	             .filter(item -> item.getName().equals(userName))
	             .findAny();  
	  return userOptional.get();
	}
	
	//Get all accountInfo instances from the repository
	public Collection<User> findAll(){
	 return Collections.unmodifiableCollection(this.userRepository);
	}

}