package com.system.fcfs.prototype.service;

import com.system.fcfs.prototype.constant.Event;
import com.system.fcfs.prototype.domain.Coupon;
import com.system.fcfs.prototype.domain.CouponCount;
import com.system.fcfs.prototype.domain.WinnerLog;
import com.system.fcfs.prototype.repository.CouponRepository;
import com.system.fcfs.prototype.repository.UserRepository;
import com.system.fcfs.prototype.repository.WinnerLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponService {

    private static final long FIRST_ELEMENT = 0;
    private static final long LAST_ELEMENT = -1;
    private static final long PUBLISH_SIZE = 10;
    private static final long LAST_INDEX = 1;
    private final RedisTemplate<String, String> redisTemplate;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final WinnerLogRepository winnerLogRepository;
    private CouponCount eventCount;

    public void setCouponCount(Event event, int queue) {
        // redisTemplate.opsForValue().set(event + "_COUNT", String.valueOf(queue));
        this.eventCount = new CouponCount(event, queue);
    }

    public void addQueue(Event event) {
        final String people = Thread.currentThread().getName();
        final long now = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(event.getName(), people, (int) now);
        log.info("대기열에 추가 - {} ({}초)", people, now);
    }

    // 기프티콘을 발급하는 메서드
    public void publish(Event event) {
        final long start = FIRST_ELEMENT;
        final long end = PUBLISH_SIZE - LAST_INDEX;

        // 현재 대기열을 조회해서 특정 갯수 만큼 빼낸다.
        Set<String> winners = redisTemplate.opsForZSet().range(event.getName(), 0, 4);

        Coupon publishCoupon = couponRepository.findCouponByEvent(event);
        for (String people : winners) {
            winnerLogRepository.save(WinnerLog.builder()
                    .coupon(publishCoupon.toString())
                    .winner(people)
                    .build());
            log.info("'{}'님의 {} 기프티콘이 발급되었습니다", people, event.getName());
            redisTemplate.opsForZSet().remove(event.getName(), people);
            //redisTemplate.opsForValue().decrement(event + "_COUNT");
            this.eventCount.decrease();
        }
    }

    public boolean validEnd(Event event) {
        log.info("==== 유효성 검사 중입니다 ==== {} 남았습니다.", eventCount.getLimit());
//        String rest = redisTemplate.opsForValue().get(event + "_COUNT");
//        int output = Integer.parseInt(rest);
//        return output == 0 ? true : false;
        if(this.eventCount.getLimit() > 0){
            return false;
        }else{
            return true;
        }
//        return this.eventCount != null
//                ? this.eventCount.end()
//                : false;
    }

    public long getSize(Event event) {
        return redisTemplate.opsForZSet().size(event.toString());
    }
}
