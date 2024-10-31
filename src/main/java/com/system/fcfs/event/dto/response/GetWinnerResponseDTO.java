package com.system.fcfs.event.dto.response;

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
