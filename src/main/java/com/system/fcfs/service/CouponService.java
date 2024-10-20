package com.system.fcfs.service;

import com.system.fcfs.infra.CouponCreateProducer;
import com.system.fcfs.infra.CouponCountRepository;
import com.system.fcfs.infra.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;

    public void apply(Long memberId){
        Long count = couponCountRepository.increase();
        if(count > 100){
            return;
        }
        couponCreateProducer.create(memberId);
    }
}
