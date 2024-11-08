package com.system.fcfs.event.implementation;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

    public List<GetWinnerResponseDTO> toWinnerResponseDTO(List<Winner> winners) {
        return winners.stream()
                .map(winner -> GetWinnerResponseDTO.builder()
                        .timeStamp(winner.getTimeStamp())
                        .phoneNum(winner.getTimeStamp())
                        .eventName(winner.getEventName())
                        .userName(winner.getUserName())
                        .build())
                .collect(Collectors.toList());
    }

    public GetWinnerResponseDTO toWinnerResponseDTO(Winner winner) {
        return GetWinnerResponseDTO.builder()
                .timeStamp(winner.getTimeStamp())
                .phoneNum(winner.getPhoneNum())
                .eventName(winner.getEventName())
                .userName(winner.getUserName())
                .build();
    }
}
