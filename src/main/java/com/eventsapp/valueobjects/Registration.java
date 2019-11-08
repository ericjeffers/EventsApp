package com.eventsapp.valueobjects;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="REGISTRATIONS")
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long Id;
	@Column(name="EVENT_ID")
	private long eventId;
	@Column(name="CUSTOMER_ID")
	private long customerId;
	@Column(name="REGISTRATION_DATE")
	private LocalDateTime date;
	@Column(name="NOTES")
	private String notes;
	
	public Registration() {}
	
	public Registration(long eventId, long customerId, LocalDateTime date, String notes) {
		this.eventId = eventId;
		this.customerId = customerId;
		this.date = date;
		this.notes = notes;
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
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
