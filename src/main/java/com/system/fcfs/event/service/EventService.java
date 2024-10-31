package com.system.fcfs.event.service;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.event.exception.DuplicatedException;
import com.system.fcfs.event.implementation.manager.EventMangerT;
import com.system.fcfs.event.repository.AttemptRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class EventService {

    private final AttemptRepository attemptRepository;
    private final EventMangerT eventMangerT;

    public EventService(@Qualifier("sqsAttemptRepository") AttemptRepository attemptRepository, EventMangerT eventMangerT) {
        this.attemptRepository = attemptRepository;
        this.eventMangerT = eventMangerT;
    }

    private static List<GetWinnerResponseDTO> toWinnerResponseDTO(List<Winner> winners) {
        return winners.stream()
                .map(winner -> GetWinnerResponseDTO.builder()
                        .time(winner.getTimeStamp())
                        .rank(winner.getRank())
                        .coupon(winner.getEvent())
                        .build())
                .collect(Collectors.toList());
    }

    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        if (!eventMangerT.validRequest(postEventRequestDTO)) {
            throw new DuplicatedException("중복 응모입니다.");
        }
        return attemptRepository.addQueue(postEventRequestDTO);
    }

    public List<GetWinnerResponseDTO> getTop100Winners(String eventName) {
        List<Winner> winners = attemptRepository.getTop100Winners(eventName);
        return toWinnerResponseDTO(winners);
    }
}
