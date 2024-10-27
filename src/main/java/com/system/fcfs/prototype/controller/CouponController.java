package com.system.fcfs.prototype.controller;

import com.system.fcfs.prototype.constant.Event;
import com.system.fcfs.prototype.dto.GetWinnerResponseDTO;
import com.system.fcfs.prototype.scheduler.EventScheduler;
import com.system.fcfs.prototype.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {
    private final CouponService couponService;
    private final EventScheduler eventScheduler;

    @PostMapping("/request/{eventId}")
    public ResponseEntity<String> requestCoupon(@PathVariable("eventId") String eventId, @RequestBody String userId) {
        Event event = Event.valueOf(eventId);
        couponService.addQueue(event, userId);
        return ResponseEntity.ok("쿠폰이 발급되었습니다.");
    }

    @GetMapping("/winner")
    public ResponseEntity<List<GetWinnerResponseDTO>> getWinner() {
        return ResponseEntity.ok(couponService.getTop100Winners());
    }
}

