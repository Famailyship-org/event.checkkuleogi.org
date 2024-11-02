package com.system.fcfs.event.dto.response;

import lombok.Builder;

@Builder
public record GetWinnerResponseDTO(String eventName, String timeStamp, String userName, String phoneNum) {
}
