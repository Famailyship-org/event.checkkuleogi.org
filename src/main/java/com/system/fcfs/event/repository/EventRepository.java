package com.system.fcfs.event.repository;

import com.system.fcfs.event.domain.Event;
import com.system.fcfs.event.domain.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.eventDate = :yesterday")
    Optional<Event> findPreviousDayEvent(@Param("yesterday") LocalDate yesterday);

    Optional<Event> findByEventType(EventType eventType);
}
