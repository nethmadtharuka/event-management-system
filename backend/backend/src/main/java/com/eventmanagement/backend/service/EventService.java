package com.eventmanagement.backend.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eventmanagement.backend.dto.EventDtos;
import com.eventmanagement.backend.model.Event;
import com.eventmanagement.backend.model.User;
import com.eventmanagement.backend.model.Vendor;
import com.eventmanagement.backend.repository.EventRepository;
import com.eventmanagement.backend.repository.UserRepository;
import com.eventmanagement.backend.repository.VendorRepository;

@Service
public class EventService {

	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final VendorRepository vendorRepository;

	public EventService(EventRepository eventRepository, UserRepository userRepository, VendorRepository vendorRepository) {
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
		this.vendorRepository = vendorRepository;
	}

	public EventDtos.EventResponse create(EventDtos.CreateEventRequest req) {
		Event event = new Event();
		event.setTitle(req.title);
		event.setDescription(req.description);
		event.setStartAt(req.startAt);
		event.setEndAt(req.endAt);
		event.setLocation(req.location);
		String username = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : null;
		if (username != null) {
			User creator = userRepository.findByUsername(username).orElse(null);
			event.setCreatedBy(creator);
		}
		if (req.vendorIds != null && !req.vendorIds.isEmpty()) {
			Set<Vendor> vendors = new java.util.HashSet<>(vendorRepository.findAllById(req.vendorIds));
			event.setVendors(vendors);
		}
		Event saved = eventRepository.save(event);
		return map(saved);
	}

	public List<EventDtos.EventResponse> listAll() {
		return eventRepository.findAll().stream().map(this::map).collect(Collectors.toList());
	}

	private EventDtos.EventResponse map(Event e) {
		EventDtos.EventResponse res = new EventDtos.EventResponse();
		res.id = e.getId();
		res.title = e.getTitle();
		res.description = e.getDescription();
		res.startAt = e.getStartAt();
		res.endAt = e.getEndAt();
		res.location = e.getLocation();
		res.createdBy = e.getCreatedBy() != null ? e.getCreatedBy().getUsername() : null;
		res.vendors = e.getVendors() == null ? java.util.List.of() : e.getVendors().stream().map(v -> {
			EventDtos.VendorSummary vs = new EventDtos.VendorSummary();
			vs.id = v.getId();
			vs.name = v.getName();
			vs.category = v.getCategory();
			return vs;
		}).collect(Collectors.toList());
		return res;
	}
}


