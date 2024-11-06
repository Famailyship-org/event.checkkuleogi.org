package com.system.fcfs.event.implementation.manager;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.event.service.EventProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventManger {

    private final RedisTemplate<String, String> redisTemplate;
    private final EventProducer eventProducer;

    public EventManger(RedisTemplate<String, String> redisTemplate, @Qualifier("sqsAttemptRepository") EventProducer eventProducer) {
        this.redisTemplate = redisTemplate;
        this.eventProducer = eventProducer;
    }

    public List<GetWinnerResponseDTO> toWinnerResponseDTO(List<Winner> winners) {
        return winners.stream()
                .map(winner -> GetWinnerResponseDTO.builder()
                        .timeStamp(winner.getTimeStamp())
                        .phoneNum(winner.getTimeStamp())
                        .eventName(winner.getEventName())
                        .userName(winner.getUserName())
                        .build())
                .collect(Collectors.toList());
    }
    public GetWinnerResponseDTO toWinnerResponseDTO(Winner winner) {
        return GetWinnerResponseDTO.builder()
                .timeStamp(winner.getTimeStamp())
                .phoneNum(winner.getPhoneNum())
                .eventName(winner.getEventName())
                .userName(winner.getUserName())
                .build();
    }
}
