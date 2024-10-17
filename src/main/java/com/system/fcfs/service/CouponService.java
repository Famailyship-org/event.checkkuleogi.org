package com.system.fcfs.service;

import com.system.fcfs.domain.Coupon;
import com.system.fcfs.infra.CouponCountRepository;
import com.system.fcfs.infra.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;

    public void apply(Long memberId){
        Long count = couponCountRepository.increase();
        if(count > 100){
            return;
        }
        couponRepository.save(new Coupon(memberId));
    }
}
