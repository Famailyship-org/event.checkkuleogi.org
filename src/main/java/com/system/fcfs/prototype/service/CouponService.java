package com.system.fcfs.prototype.service;

import com.system.fcfs.prototype.constant.Event;
import com.system.fcfs.prototype.domain.Winner;
import com.system.fcfs.prototype.domain.repository.WinnerRepository;
import com.system.fcfs.prototype.dto.GetWinnerResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CouponService {
    private final WinnerRepository winnerRepository;

    // 원하는 구현체를 선택하도록 @Qualifier 사용
    public CouponService(@Qualifier("jpaWinnerRepository") WinnerRepository winnerRepository) {
        this.winnerRepository = winnerRepository;
    }

    @Transactional
    public void addQueue(Event event, String userId) {
        final String people = Thread.currentThread().getName() + ", time: " + System.currentTimeMillis();
        final Instant stamp = Instant.now();
        String now = stamp.toString();
        winnerRepository.addQueue(userId, now, event);
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
        List<Winner> winners = winnerRepository.geTop100Winner(pageable);
        return winners.stream()
                .map(winner -> GetWinnerResponseDTO.builder()
                        .userId(winner.getWinner())
                        .time(winner.getTime())
                        .coupon(winner.getCoupon())
                        .build())
                .collect(Collectors.toList());
    }
}
