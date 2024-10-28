package com.system.fcfs.event.dto;

import com.system.fcfs.event.domain.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetEventResponseDTO {
    Long idx;
    String eventName;
    SiteUser winner;
    Integer price;
    String createdAt;
}
