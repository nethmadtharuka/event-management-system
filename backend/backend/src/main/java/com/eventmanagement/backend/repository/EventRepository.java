package com.eventmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eventmanagement.backend.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Override
	@EntityGraph(attributePaths = {"vendors", "createdBy"})
	List<Event> findAll();
}


