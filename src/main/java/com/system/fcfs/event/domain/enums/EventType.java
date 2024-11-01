package com.system.fcfs.event.domain.enums;

import lombok.Getter;

@Getter
public enum EventType {
    MONTH_BOOK_SUBSCRIBE("1달 구독권");

    private String name;
    EventType(String name){
        this.name = name;
    }
}
