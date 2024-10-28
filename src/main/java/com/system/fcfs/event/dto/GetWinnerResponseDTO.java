package com.system.fcfs.event.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetWinnerResponseDTO {
    private String userId;
    private String time;
    private String coupon;
    private Long rank;
}
