package com.system.fcfs.prototype.controller;

import com.system.fcfs.prototype.constant.Event;
import com.system.fcfs.prototype.repository.UserRepository;
import com.system.fcfs.prototype.scheduler.EventScheduler;
import com.system.fcfs.prototype.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {
    private final CouponService couponService;
    private final UserRepository userRepository;
    private final EventScheduler eventScheduler;

    // 특정 이벤트에 대해서 요청하는 post
    @PostMapping("/request/{eventId}")
    public ResponseEntity<String> requestCoupon(@PathVariable("eventId") String eventId) {

        // 다양한 유효성 검사
        // 1. 사용자 인증
        // 2. 쿠폰을 이미 받았는지 확인

        Event event = Event.valueOf(eventId);
        eventScheduler.setEvent(event);
        couponService.addQueue(event);

        return ResponseEntity.ok("쿠폰이 발급되었습니다.");
    }
}

