package com.eventmanagement.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.backend.dto.EventDtos;
import com.eventmanagement.backend.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
@Validated
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@PostMapping
	public ResponseEntity<EventDtos.EventResponse> create(@Valid @RequestBody EventDtos.CreateEventRequest request) {
		return ResponseEntity.ok(eventService.create(request));
	}

	@GetMapping
	public ResponseEntity<List<EventDtos.EventResponse>> list() {
		return ResponseEntity.ok(eventService.listAll());
	}
}


