package com.system.fcfs.event.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class PostEventRequestDTO {
    private final String userName;
    private String timestamp;
    private final String eventName;
    private final String phoneNum;

    @Override
    public String toString() {
        return "{" +
                "\"userName\":\"" + userName + "\"," +
                "\"phoneNum\":\"" + phoneNum + "\"," +
                "\"eventName\":\"" + eventName + "\"," +
                "\"timeStamp\":\"" + timestamp + "\"" +
                "}";
    }


    public String setTimeStamp(String timeStamp) {
        return this.timestamp = timeStamp;
    }
}