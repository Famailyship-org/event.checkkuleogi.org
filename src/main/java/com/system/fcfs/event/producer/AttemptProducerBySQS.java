package com.system.fcfs.event.producer;

import com.system.fcfs.event.consumer.SqsMessageListener;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sqsAttemptRepository")
@RequiredArgsConstructor
public class AttemptProducerBySQS implements AttemptProducer {
    private static final String HYPER_LOG_LOG_KEY = "userPhoneCheck";
    private final SqsMessageSender sqsMessageSender;
    private final SqsMessageListener sqsMessageListener;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<Winner> getTop100AndUpdateQueue(String eventName) {
        List<Winner> winners = sqsMessageListener.receiveAndSaveWinners();
        sqsMessageListener.receiveAndSaveAllAttempts();
        return winners;
    }

    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        String uniqueKey = postEventRequestDTO.userId() + ":" + postEventRequestDTO.eventName();

        // Redis HyperLogLog에 추가하고, 추가 전후 크기 비교
        Long previousCount = redisTemplate.opsForHyperLogLog().size(HYPER_LOG_LOG_KEY);
        redisTemplate.opsForHyperLogLog().add(HYPER_LOG_LOG_KEY, uniqueKey);
        Long currentCount = redisTemplate.opsForHyperLogLog().size(HYPER_LOG_LOG_KEY);

        // 추가 전후 크기가 같다면 중복으로 간주
        return previousCount != null && previousCount.equals(currentCount);
    }

    @Override
    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        String message = postEventRequestDTO.toString();
        return sqsMessageSender.sendMessage(message) != null;
    }
}
