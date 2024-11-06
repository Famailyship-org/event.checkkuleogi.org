package com.system.fcfs.event.implementation;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.event.exception.DuplicatedException;
import com.system.fcfs.event.service.EventConsumer;
import com.system.fcfs.event.implementation.manager.EventManger;
import com.system.fcfs.event.service.EventProducer;
import com.system.fcfs.event.service.EventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class EventServiceImpl implements EventService {

    private final EventConsumer eventConsumer;
    private final EventProducer eventProducer;
    private final EventManger eventManger;

    public EventServiceImpl(@Qualifier("redisAttemptRepository") EventProducer eventProducer
            , EventManger eventManger
            , @Qualifier("rdbEventConsumerByJPA") EventConsumer eventConsumer) {
        this.eventConsumer = eventConsumer;
        this.eventProducer = eventProducer;
        this.eventManger = eventManger;
    }

    public Boolean addJobQ(PostEventRequestDTO postEventRequestDTO) {
        if (eventProducer.validRequest(postEventRequestDTO)) {
            throw new DuplicatedException("중복 응모입니다.");
        }
        return eventProducer.addQueue(postEventRequestDTO);
    }

    public List<GetWinnerResponseDTO> consumeJobQ(String eventName) {
        List<Winner> winners = eventConsumer.getTop100AndUpdateQueue(eventName);
        return eventManger.toWinnerResponseDTO(winners);
    }

    public GetWinnerResponseDTO getWinner(GetWinnerRequestDTO getWinnerRequestDTO) {
            Winner winner = eventConsumer.getWinner(getWinnerRequestDTO);
            return eventManger.toWinnerResponseDTO(winner);
    }
}
