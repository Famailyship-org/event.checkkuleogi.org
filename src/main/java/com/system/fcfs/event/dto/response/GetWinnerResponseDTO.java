package com.system.fcfs.event.dto.response;

import lombok.Builder;

@Builder
public record GetWinnerResponseDTO(String userId, String eventName, String timeStamp, String coupon, String phoneNum) {
}
