package com.system.fcfs.event.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostEventRequestDTO {
    private String userId;
    private Long rank;
    private String timestamp;
    private String eventName;

    public void setRank(Long rank) {
        this.rank = rank;
    }
}
