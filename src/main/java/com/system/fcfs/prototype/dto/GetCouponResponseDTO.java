package com.system.fcfs.prototype.dto;

import com.system.fcfs.prototype.domain.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetCouponResponseDTO {
    Long idx;
    String eventName;
    SiteUser winner;
    Integer price;
    String createdAt;
}
