package com.system.fcfs.event.service;

import com.system.fcfs.event.dto.request.PostEventRequestDTO;

public interface EventProducer {
    Boolean validRequest(PostEventRequestDTO postEventRequestDTO);

    Boolean addJobQ(PostEventRequestDTO postEventRequestDTO);
}
