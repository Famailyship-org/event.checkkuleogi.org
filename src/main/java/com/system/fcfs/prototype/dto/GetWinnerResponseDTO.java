package com.system.fcfs.prototype.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetWinnerResponseDTO {
    private String userId;
    private String time;
    private String coupon;
}
