package com.eventsapp.valueobjects;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity
@Table(name="REGISTRATIONS")
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@Column(name="EVENT_ID")
	private long eventId;
	@Column(name="CUSTOMER_ID")
	private long customerId;
	@Column(name="REGISTRATION_DATE")
	private Date registrationDate;
	@Column(name="NOTES")
	private String notes;
	
	public Registration() {}
	
	public Registration(long eventId, long customerId, Date date, String notes) {
		this.eventId = eventId;
		this.customerId = customerId;
		this.registrationDate = date;
		this.notes = notes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public static RegistrationMedium convertRegistrationToMedium(Registration registration) {
		RegistrationMedium regMed = new RegistrationMedium();
		
		regMed.setId(registration.getId());
		regMed.setEvent_id(registration.getEventId());
		regMed.setCustomer_id(registration.getCustomerId());
		regMed.setRegistration_date(registration.getRegistrationDate());
		regMed.setNotes(registration.getNotes());
		
		return regMed;
	}
	
}
