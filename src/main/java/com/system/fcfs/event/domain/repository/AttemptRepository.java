package com.system.fcfs.event.domain.repository;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.dto.PostEventRequestDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttemptRepository {

    List<Attempt> geTop100Attempt(Pageable pageable);

    boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO);

    void addQueue(PostEventRequestDTO postEventRequestDTO);
}
