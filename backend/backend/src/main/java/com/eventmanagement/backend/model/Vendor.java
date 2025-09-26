package com.eventmanagement.backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendors")
public class Vendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(length = 255)
	private String category; // catering, decoration, music, etc.

	@Column(length = 20)
	private String phone;

	@Column(length = 255)
	private String email;

	@Column(length = 500)
	private String description;

	@ManyToMany(mappedBy = "vendors")
	private Set<Event> events = new HashSet<>();

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public Set<Event> getEvents() { return events; }
	public void setEvents(Set<Event> events) { this.events = events; }
}


