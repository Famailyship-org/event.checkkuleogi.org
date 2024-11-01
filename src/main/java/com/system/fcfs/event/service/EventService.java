package com.system.fcfs.event.service;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.event.exception.DuplicatedException;
import com.system.fcfs.event.implementation.manager.EventMangerT;
import com.system.fcfs.event.producer.AttemptProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class EventService {

    private final AttemptProducer attemptProducer;
    private final EventMangerT eventMangerT;

    public EventService(@Qualifier("sqsAttemptRepository") AttemptProducer attemptProducer, EventMangerT eventMangerT) {
        this.attemptProducer = attemptProducer;
        this.eventMangerT = eventMangerT;
    }

    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        if (!attemptProducer.validRequest(postEventRequestDTO)) {
            throw new DuplicatedException("중복 응모입니다.");
        }
        return attemptProducer.addQueue(postEventRequestDTO);
    }

    public List<GetWinnerResponseDTO> processScheduledQueue(String eventName) {
        List<Winner> winners = attemptProducer.getTop100AndUpdateQueue(eventName);
        return eventMangerT.toWinnerResponseDTO(winners);
    }
}
