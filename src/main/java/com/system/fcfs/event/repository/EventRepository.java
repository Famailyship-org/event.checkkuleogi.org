package com.system.fcfs.event.repository;

import com.system.fcfs.event.domain.Event;
import com.system.fcfs.event.domain.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByEventType(EventType eventType);
}
