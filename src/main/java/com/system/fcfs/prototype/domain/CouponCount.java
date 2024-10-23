package com.system.fcfs.prototype.domain;

import com.system.fcfs.prototype.constant.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponCount {
    private Event event;
    private int limit;

    private static final int END = 0;

    public CouponCount(Event event, int limit){
        this.event = event;
        this.limit = limit;
    }

    public synchronized void decrease(){
        this.limit--;
    }
    public boolean end(){
        return this.limit == END;
    }
}
