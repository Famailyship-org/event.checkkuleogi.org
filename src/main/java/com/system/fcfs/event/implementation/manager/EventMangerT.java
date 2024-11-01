package com.system.fcfs.event.implementation.manager;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.event.producer.AttemptProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMangerT {

    private final RedisTemplate<String, String> redisTemplate;
    private final AttemptProducer attemptProducer;

    public EventMangerT(RedisTemplate<String, String> redisTemplate, @Qualifier("sqsAttemptRepository") AttemptProducer attemptProducer) {
        this.redisTemplate = redisTemplate;
        this.attemptProducer = attemptProducer;
    }

    public List<GetWinnerResponseDTO> toWinnerResponseDTO(List<Winner> winners) {
        return winners.stream()
                .map(winner -> GetWinnerResponseDTO.builder()
                        .timeStamp(winner.getTimeStamp())
                        .phoneNum(winner.getTimeStamp())
                        .build())
                .collect(Collectors.toList());
    }
}
