package com.eventsapp.valueobjects;

import java.sql.Date;

public class RegistrationMedium {
	
	long id;
	private long event_id;
	private long customer_id;
	private Date registration_date;
	private String notes;
	
	public RegistrationMedium() {}
	
	public RegistrationMedium(long eventId, long customerId, Date date, String notes) {
		this.event_id = eventId;
		this.customer_id = customerId;
		this.registration_date = date;
		this.notes = notes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEvent_id() {
		return event_id;
	}

	public void setEvent_id(long event_id) {
		this.event_id = event_id;
	}

	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

	public Date getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public static Registration convertMediumToRegistration(RegistrationMedium regMed) {
		Registration registration = new Registration();
		
		registration.setId(regMed.getId());
		registration.setEventId(regMed.getEvent_id());
		registration.setCustomerId(regMed.getCustomer_id());
		registration.setRegistrationDate(regMed.getRegistration_date());
		registration.setNotes(regMed.getNotes());
		
		return registration;
	}
	
}