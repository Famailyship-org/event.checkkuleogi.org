package com.system.fcfs.event.implementation;

import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.event.service.EventConsumer;
import com.system.fcfs.event.service.EventProducer;
import com.system.fcfs.event.presentation.EventUseCase;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EventService implements EventUseCase {

    private final EventConsumer eventConsumer;
    private final EventProducer eventProducer;
    private final DtoMapper dtoMapper;

    public EventService(@Qualifier("noSqlEventProducerByRedis") EventProducer eventProducer
            , @Qualifier("noSqlEventConsumerByRedis") EventConsumer eventConsumer, DtoMapper dtoMapper) {
        this.eventConsumer = eventConsumer;
        this.eventProducer = eventProducer;
        this.dtoMapper = dtoMapper;
    }

    public Boolean addJobQ(PostEventRequestDTO postEventRequestDTO) {
        return eventProducer.addJobQ(postEventRequestDTO);
    }

    public Boolean consumeJobQ(String eventName) {
        return eventConsumer.consumeJobQ(eventName);
    }

    public GetWinnerResponseDTO getWinner(GetWinnerRequestDTO getWinnerRequestDTO) {
        return dtoMapper.toWinnerResponseDTO(eventConsumer.getWinner(getWinnerRequestDTO));
    }
}
