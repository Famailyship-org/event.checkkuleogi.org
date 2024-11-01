package com.system.fcfs.event.dto.request;

import lombok.Builder;

@Builder
public record PostEventRequestDTO(String userId, String timestamp, String eventName, String phoneNum) {
    @Override
    public String toString() {
        return "{" +
                "\"userId\":\"" + userId + "\"," +
                "\"timestamp\":\"" + timestamp + "\"," +
                "\"phoneNum\":\"" + phoneNum + "\"," +
                "\"eventName\":\"" + eventName + "\"" +
                "}";
    }
}
