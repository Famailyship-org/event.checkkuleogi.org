package com.system.fcfs.event.service;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;

import java.util.List;

public interface EventConsumer {
     List<Winner> getTop100AndUpdateQueue(String eventName);

    Winner getWinner(GetWinnerRequestDTO getWinnerRequestDTO);
}
