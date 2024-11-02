package com.system.fcfs.event.producer;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;

import java.util.List;

public interface AttemptProducer {

    List<Winner> getTop100AndUpdateQueue(String eventName);

    Boolean validRequest(PostEventRequestDTO postEventRequestDTO);

    Boolean addQueue(PostEventRequestDTO postEventRequestDTO);

    Winner getWinner(GetWinnerRequestDTO getWinnerRequestDTO);
}
