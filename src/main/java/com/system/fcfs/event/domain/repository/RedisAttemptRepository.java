package com.system.fcfs.event.domain.repository;


import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.dto.PostEventRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("redisAttemptRepository")
public class RedisAttemptRepository implements AttemptRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisAttemptRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Attempt> geTop100Attempt(Pageable pageable) {
        return List.of();
    }

    @Override
    public boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO) {
        return redisTemplate.opsForSet().isMember(postEventRequestDTO.getEventName()
                , postEventRequestDTO.getUserId());
    }

    @Override
    public void addQueue(PostEventRequestDTO postEventRequestDTO) {
        // 해당 이벤트에 대해 사용자를 큐에 넣는다.
        // 그런데 그냥 set에 넣어도 되겠지? 왜냐하면 정렬자체는 나중에 할꺼니깐. -- 시간은 그냥 넣을 필요가 없다
        redisTemplate.opsForZSet().add(postEventRequestDTO.getEventName()
                , postEventRequestDTO.getUserId()
                , Double.parseDouble(postEventRequestDTO.getTimestamp()));
    }
}
