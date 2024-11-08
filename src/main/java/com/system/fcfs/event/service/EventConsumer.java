package com.system.fcfs.event.service;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;

import java.util.List;

public interface EventConsumer {
    Winner getWinner(GetWinnerRequestDTO getWinnerRequestDTO);
    Boolean consumeJobQ(String eventName);
}
