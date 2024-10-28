package com.system.fcfs.event.scheduler;

import com.system.fcfs.event.constant.Event;
import com.system.fcfs.event.service.EventService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class EventScheduler {
    private final EventService couponService;

    // 컨트롤러에서 호출하여 현재 이벤트를 설정하는 메소드
    @PostConstruct
    private void init() {
        Event event = Event.FREE_CAMPING;
        int initialCouponCount = 50;
        // 서비스에서 couponCount 초기화
        log.info("이벤트 {}의 초기 쿠폰 수량이 {}으로 설정되었습니다.", event, initialCouponCount);
    }

//    @Scheduled(fixedDelay = 2000)
//    private void chickenEventScheduler() {
//        if (couponService.validEnd(event)) {
//            log.info("==== 선착순 이벤트가 종료되었습니다. ====");
//            return;
//        } else {
//            // 종료되지 않았으면 기프티콘을 발급하고 남은 대기열에 순번 표출
//            couponService.publish(event);
//            // couponService.getOrder(Event.CHICKEN);
//        }
//    }
}
