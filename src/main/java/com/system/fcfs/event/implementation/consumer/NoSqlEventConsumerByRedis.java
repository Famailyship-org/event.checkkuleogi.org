package com.system.fcfs.event.implementation.consumer;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.repository.JpaEventRepository;
import com.system.fcfs.event.repository.WinnerRepository;
import com.system.fcfs.event.service.EventConsumer;
import com.system.fcfs.global.domain.exception.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Component("noSqlEventConsumerByRedis")
public class NoSqlEventConsumerByRedis implements EventConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private final JpaEventRepository jpaEventRepository;
    private final WinnerRepository winnerRepository;

    public NoSqlEventConsumerByRedis(RedisTemplate<String, String> redisTemplate, JpaEventRepository jpaEventRepository, WinnerRepository winnerRepository) {
        this.redisTemplate = redisTemplate;
        this.jpaEventRepository = jpaEventRepository;
        this.winnerRepository = winnerRepository;
    }

    @Override
    public Winner getWinner(GetWinnerRequestDTO getWinnerRequestDTO) {
        return winnerRepository.findByUserNameAndPhoneNum(getWinnerRequestDTO.userName(),
                getWinnerRequestDTO.phoneNum()).orElseThrow(() -> new NotFoundException("당첨자가 없습니다."));
    }

    @Override
    public Boolean consumeJobQ(String eventName) {
        Set<String> top100Result = redisTemplate.opsForZSet().range(eventName, 0, 99);

        if (top100Result == null || top100Result.isEmpty()) {
            throw new NotFoundException("당첨자가 없습니다.");
        }
        Set<String> remainingResult = redisTemplate.opsForZSet().range(eventName, 100, -1);
        if (remainingResult == null || remainingResult.isEmpty()) {
            throw new NotFoundException("당첨자가 없습니다.");
        }

        log.info("Top 100 result: {}", top100Result);
        log.info("Remaining result: {}", remainingResult);

        List<Winner> winners = top100Result.stream()
                .map(winnerStr -> {
                    String uuid = UUID.randomUUID().toString();  // 고유한 UUID 생성
                    double score = redisTemplate.opsForZSet().score(eventName, winnerStr); // timestamp 값 가져오기

                    // Winner 객체에 UUID, 이름, 전화번호, timestamp 추가
                    Winner winner = Winner.builder()
                            .uuid(uuid)
                            .userName(winnerStr.split("\\|")[0])
                            .phoneNum(winnerStr.split("\\|")[1])
                            .timeStamp(Double.toString(score))
                            .eventName(eventName)
                            .build();
                    winnerRepository.save(winner);
                    return winner;
                })
                .collect(Collectors.toList());

        remainingResult.forEach(winnerStr -> {
            double score = redisTemplate.opsForZSet().score(eventName, winnerStr); // timestamp 값 가져오기

            // Attempt 객체에 UUID, 이름, 전화번호, timestamp 추가
            Attempt attempt = Attempt.builder()
                    .userName(winnerStr.split("\\|")[0])
                    .phoneNum(winnerStr.split("\\|")[1])
                    .timeStamp(Double.toString(score))
                    .eventName(eventName)
                    .build();
            jpaEventRepository.save(attempt);
        });
        redisTemplate.delete(eventName);
        return true;
    }
}