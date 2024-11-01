package com.system.fcfs.event.dto.response;

import com.system.fcfs.event.domain.SiteUser;
import lombok.Builder;

@Builder
public record GetEventResponseDTO(Long idx, String eventName, SiteUser winner, Integer price, String createdAt) {
}
