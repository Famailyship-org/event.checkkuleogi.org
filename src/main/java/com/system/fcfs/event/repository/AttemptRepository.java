package com.system.fcfs.event.repository;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttemptRepository {

    List<Winner> geTop100Attempt(Pageable pageable, String eventName);

    boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO);

    void addQueue(PostEventRequestDTO postEventRequestDTO);

}
