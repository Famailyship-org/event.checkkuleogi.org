package com.system.fcfs.event.implementation.producer;

import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.service.EventProducer;
import com.system.fcfs.global.domain.exception.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component("noSqlEventProducerByRedis")
public class NoSqlEventProducerByRedis implements EventProducer {

    private final RedisTemplate<String, String> redisTemplate;

    public NoSqlEventProducerByRedis(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        String uniqueKey = postEventRequestDTO.getUserName() + "|" + postEventRequestDTO.getPhoneNum();
        Double score = redisTemplate.opsForZSet().score(postEventRequestDTO.getEventName(), uniqueKey);
        return score != null; // score가 null이 아니면 중복된 멤버
    }

    @Override
    public Boolean addJobQ(PostEventRequestDTO postEventRequestDTO) {
        log.info("Request: {}", postEventRequestDTO);

        if(validRequest(postEventRequestDTO)){
            throw new NotFoundException("중복 응모입니다.");
        }
        double time = System.currentTimeMillis();
        try {
            boolean isAdded = redisTemplate.opsForZSet().add(
                    postEventRequestDTO.getEventName(),
                    postEventRequestDTO.getUserName() + "|" + postEventRequestDTO.getPhoneNum(),
                    time
            );
            if (!isAdded) {
                log.error("이벤트 추가 실패: {}", postEventRequestDTO.getEventName());
                return false;
            }
        } catch (Exception e) {
            log.error("레디스 Zset 추가 시 실패", e);
            throw new RuntimeException("레디스 데이터 추가시 실패", e);
        }
        return true;
    }
}