package com.system.fcfs.event.implementation.manager;

import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.producer.SqsMessageSender;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@AllArgsConstructor
public class EventMangerT {

    private static final String HYPER_LOG_LOG_KEY = "userPhoneCheck";
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        String uniqueKey = postEventRequestDTO.getUserId() + ":" + postEventRequestDTO.getPhoneNum();

        // Redis HyperLogLog에 추가하고, 추가 전후 크기 비교
        Long previousCount = redisTemplate.opsForHyperLogLog().size(HYPER_LOG_LOG_KEY);
        redisTemplate.opsForHyperLogLog().add(HYPER_LOG_LOG_KEY, uniqueKey);
        Long currentCount = redisTemplate.opsForHyperLogLog().size(HYPER_LOG_LOG_KEY);

        // 추가 전후 크기가 같다면 중복으로 간주
        return previousCount != null && currentCount != null && previousCount.equals(currentCount);
    }
}
