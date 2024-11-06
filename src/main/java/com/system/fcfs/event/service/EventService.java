package com.system.fcfs.event.service;

import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;

import java.util.List;

public interface EventService {

    Boolean addJobQ(PostEventRequestDTO postEventRequestDTO);

    List<GetWinnerResponseDTO> consumeJobQ(String eventName);

    GetWinnerResponseDTO getWinner(GetWinnerRequestDTO getWinnerRequestDTO);
}
