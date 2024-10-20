package com.system.fcfs.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponCreatedConsumer {
    private final CouponRepository couponRepository;

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long memberId){
        System.out.println("memberId = " + memberId);
        // couponRepository.save(new Coupon(memberId));
    }
}
