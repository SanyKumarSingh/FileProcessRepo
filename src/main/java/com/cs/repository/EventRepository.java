package com.cs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs.model.Event;

public interface EventRepository extends JpaRepository<Event, String> {

}
