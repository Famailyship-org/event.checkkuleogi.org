package com.system.fcfs.event.producer;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.repository.AttemptJpaRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Repository("redisAttemptRepository")
public class AttemptProducerByRedis implements AttemptProducer {
    private final RedisTemplate<String, String> redisTemplate;
    private final AttemptJpaRepository attemptJpaRepository;

    public AttemptProducerByRedis(RedisTemplate<String, String> redisTemplate, AttemptJpaRepository attemptJpaRepository) {
        this.redisTemplate = redisTemplate;
        this.attemptJpaRepository = attemptJpaRepository;
    }

    @Override
    public List<Winner> getTop100AndUpdateQueue(String eventName) {
        Set<String> top100Result = redisTemplate.opsForZSet().range(eventName, 0, 99);
        Set<String> remainingResult = redisTemplate.opsForZSet().range(eventName, 100, -1);

        log.info("Top 100 result: {}", top100Result);
        log.info("Remaining result: {}", remainingResult);

        List<Winner> winners = top100Result.stream()
                .map(winnerStr -> Winner.builder()
                        .timeStamp(redisTemplate.opsForZSet().score(eventName, winnerStr).toString())
                        .build())
                .collect(Collectors.toList());

        remainingResult.forEach(winnerStr -> {
            Attempt attempt = Attempt.builder()
                    .userName(winnerStr.split("\\|")[0])
                    .timeStamp(redisTemplate.opsForZSet().score(eventName, winnerStr).toString())
                    .phoneNum(winnerStr.split("\\|")[1])
                    .build();
            attemptJpaRepository.save(attempt);
        });
        return winners;
    }

    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        return redisTemplate.opsForSet().isMember(postEventRequestDTO.eventName()
                , postEventRequestDTO.userId());
    }

    @Override
    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        redisTemplate.opsForZSet().add(
                postEventRequestDTO.eventName(),
                postEventRequestDTO.userId() + "|" + postEventRequestDTO.phoneNum(),
                Double.parseDouble(postEventRequestDTO.timestamp())
        );
        return true;
    }
}