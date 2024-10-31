package com.system.fcfs.event.repository;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;

import java.util.List;

public interface AttemptRepository {

    List<Winner> getTop100Winners(String eventName);

    Boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO);

    Boolean addQueue(PostEventRequestDTO postEventRequestDTO);

}
