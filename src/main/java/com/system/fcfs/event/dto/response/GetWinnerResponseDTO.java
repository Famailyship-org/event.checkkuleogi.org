package com.system.fcfs.event.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record GetWinnerResponseDTO (String userId, String eventName, String timeStamp, String coupon, String phoneNum){
}
