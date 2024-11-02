package com.system.fcfs.event.producer;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.repository.AttemptJpaRepository;
import com.system.fcfs.event.repository.WinnerRepository;
import com.system.fcfs.global.domain.exception.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Repository("redisAttemptRepository")
public class AttemptProducerByRedis implements AttemptProducer {

    private final RedisTemplate<String, String> redisTemplate;
    private final AttemptJpaRepository attemptJpaRepository;
    private final WinnerRepository winnerRepository;

    public AttemptProducerByRedis(RedisTemplate<String, String> redisTemplate, AttemptJpaRepository attemptJpaRepository, WinnerRepository winnerRepository) {
        this.redisTemplate = redisTemplate;
        this.attemptJpaRepository = attemptJpaRepository;
        this.winnerRepository = winnerRepository;
    }

    public List<Winner> getTop100AndUpdateQueue(String eventName) {
        Set<String> top100Result = redisTemplate.opsForZSet().range(eventName, 0, 99);
        Set<String> remainingResult = redisTemplate.opsForZSet().range(eventName, 100, -1);

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
            attemptJpaRepository.save(attempt);
        });

        // 마지막에 Redis 삭제
        redisTemplate.delete(eventName);
        return winners;
    }



    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        String uniqueKey = postEventRequestDTO.getUserName() + "|" + postEventRequestDTO.getPhoneNum();
        Double score = redisTemplate.opsForZSet().score(postEventRequestDTO.getEventName(), uniqueKey);
        return score != null; // score가 null이 아니면 중복된 멤버
    }
    @Override
    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        log.info("Request: {}", postEventRequestDTO);
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

    @Override
    public Winner getWinner(GetWinnerRequestDTO getWinnerRequestDTO) {
        return winnerRepository.findByUserNameAndPhoneNum(getWinnerRequestDTO.userName(),
                getWinnerRequestDTO.phoneNum()).orElseThrow(() -> new NotFoundException("당첨자가 없습니다."));
    }
}