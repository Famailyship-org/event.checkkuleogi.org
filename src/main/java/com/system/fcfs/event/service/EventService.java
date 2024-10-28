package com.system.fcfs.event.service;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.repository.AttemptRepository;
import com.system.fcfs.event.dto.GetWinnerResponseDTO;
import com.system.fcfs.event.dto.PostEventRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class EventService {
    private final AttemptRepository attemptRepository;

    // 원하는 구현체를 선택하도록 @Qualifier 사용
    public EventService(@Qualifier("redisAttemptRepository") AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    @Transactional
    public void addQueue(PostEventRequestDTO postEventRequestDTO) {
        attemptRepository.addQueue(postEventRequestDTO);
    }

    // 기프티콘을 발급하는 메서드
//    public void publish(Event event) {
//        final long start = FIRST_ELEMENT;
//        final long end = PUBLISH_SIZE - LAST_INDEX;
//
//        // 현재 대기열을 조회해서 특정 갯수 만큼 빼낸다.
////        Set<String> winners = redisTemplate.opsForZSet().range(event.getName(), 0, 4);
////        winnerRepository.getWinner(winners, event);
//
//        Coupon publishCoupon = couponRepository.findCouponByEvent(event);
//        for (String people : winners) {
//            winnerRepository.save(Winner.builder()
//                    .coupon(publishCoupon.toString())
//                    .winner(people)
//                    .build());
//            log.info("'{}'님의 {} 기프티콘이 발급되었습니다", people, event.getName());
//            // redisTemplate.opsForZSet().remove(event.getName(), people);
//        }
//    }

    // 당첨자를 조회하는 메서드
    public List<GetWinnerResponseDTO> getTop100Winners() {
        Pageable pageable = PageRequest.of(0, 100);
        List<Attempt> attempts = attemptRepository.geTop100Attempt(pageable);
        return attempts.stream()
                .map(attempt -> GetWinnerResponseDTO.builder()
                        .userId(attempt.getUserName())
                        .time(attempt.getTimeStamp())
                        .rank(attempt.getRank())
                        .coupon(attempt.getCoupon())
                        .build())
                .collect(Collectors.toList());
    }

    public void validRequest(PostEventRequestDTO userId) {
        // 1. 해당 유저가 이미 참여했는지 확인
        if (attemptRepository.existsByAttemptAndEvent(userId)) {
            throw new IllegalArgumentException("이미 참여한 유저입니다.");
        }
    }
}
