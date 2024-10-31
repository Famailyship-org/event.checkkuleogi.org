package com.system.fcfs.event.repository;


import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Log4j2
@Repository("redisAttemptRepository")
public class RedisAttemptRepository implements AttemptRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisAttemptRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Winner> getTop100Winners(String eventName) {
        Set<String> result = redisTemplate.opsForZSet().range(eventName, 0, 99);
        log.info("result: {}", result);
        List<Winner> winners = result.stream()
                .map(winner -> Winner.builder()
                        .winner(winner)
                        .timeStamp(redisTemplate.opsForZSet().score(eventName, winner).toString())
                        .rank(redisTemplate.opsForZSet().rank(eventName, winner))
                        .build())
                .toList();
        return winners;
    }

    @Override
    public Boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO) {
        return redisTemplate.opsForSet().isMember(postEventRequestDTO.getEventName()
                , postEventRequestDTO.getUserId());
    }

    @Override
    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        redisTemplate.opsForZSet().add(postEventRequestDTO.getEventName()
                , postEventRequestDTO.getUserId()
                , Double.parseDouble(postEventRequestDTO.getTimestamp()));
        return true;
    }
}
