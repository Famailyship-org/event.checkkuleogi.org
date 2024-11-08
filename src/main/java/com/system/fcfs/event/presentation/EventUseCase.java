package com.system.fcfs.event.presentation;

import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;

public interface EventUseCase {

    Boolean addJobQ(PostEventRequestDTO postEventRequestDTO);

    Boolean consumeJobQ(String eventName);

    GetWinnerResponseDTO getWinner(GetWinnerRequestDTO getWinnerRequestDTO);
}
