package com.system.fcfs.prototype.repository;

import com.system.fcfs.prototype.constant.Event;
import com.system.fcfs.prototype.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findCouponByEvent(Event event);
}
