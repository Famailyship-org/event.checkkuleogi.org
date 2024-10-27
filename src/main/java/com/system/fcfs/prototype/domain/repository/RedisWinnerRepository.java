package com.system.fcfs.prototype.domain.repository;


import com.system.fcfs.prototype.constant.Event;
import com.system.fcfs.prototype.domain.Winner;
import com.system.fcfs.prototype.dto.GetWinnerResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class RedisWinnerRepository implements WinnerRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisWinnerRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Winner> findAllWinners() {
        // Redis 관련 코드
        return null;
    }


    @Override
    public Set<String> findWinnerByIdx(Long userIdx, Event event) {
        Set<String> winners = redisTemplate.opsForZSet().range(event.getName(), 0, 4);
        return winners;
    }

    @Override
    public void addQueue(String people, String now, Event event) {
        redisTemplate.opsForZSet().add(event.getName(), people, Double.parseDouble(now));
    }

    @Override
    public List<Winner> geTop100Winner(Pageable pageable) {
            return List.of();
    }
}
