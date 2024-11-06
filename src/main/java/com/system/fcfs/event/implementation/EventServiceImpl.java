package com.system.fcfs.event.implementation;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.event.exception.DuplicatedException;
import com.system.fcfs.event.implementation.manager.EventManger;
import com.system.fcfs.event.producer.AttemptProducer;
import com.system.fcfs.event.service.EventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class EventServiceImpl implements EventService {

    private final AttemptProducer attemptProducer;
    private final EventManger eventManger;

    public EventServiceImpl(@Qualifier("redisAttemptRepository") AttemptProducer attemptProducer, EventManger eventManger) {
        this.attemptProducer = attemptProducer;
        this.eventManger = eventManger;
    }

    public Boolean addJobQ(PostEventRequestDTO postEventRequestDTO) {
        if (attemptProducer.validRequest(postEventRequestDTO)) {
            throw new DuplicatedException("중복 응모입니다.");
        }
        return attemptProducer.addQueue(postEventRequestDTO);
    }

    public List<GetWinnerResponseDTO> consumeJobQ(String eventName) {
        List<Winner> winners = attemptProducer.getTop100AndUpdateQueue(eventName);
        return eventManger.toWinnerResponseDTO(winners);
    }

    public GetWinnerResponseDTO getWinner(GetWinnerRequestDTO getWinnerRequestDTO) {
            Winner winner = attemptProducer.getWinner(getWinnerRequestDTO);
            return eventManger.toWinnerResponseDTO(winner);
    }
}
