package com.system.fcfs.event.service;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.repository.AttemptRepository;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class EventService {
    private final AttemptRepository attemptRepository;

    // 원하는 구현체를 선택하도록 @Qualifier 사용
    public EventService(@Qualifier("sqsAttemptRepository") AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public void addQueue(PostEventRequestDTO postEventRequestDTO) {
        attemptRepository.addQueue(postEventRequestDTO);
    }

    // 당첨자를 조회하는 메서드
    public List<GetWinnerResponseDTO> getTop100Winners(String eventName) {
        Pageable pageable = PageRequest.of(0, 100);
        List<Winner> winners = attemptRepository.geTop100Attempt(pageable, eventName);
        return winners.stream()
                .map(winner -> GetWinnerResponseDTO.builder()
                        .time(winner.getTimeStamp())
                        .rank(winner.getRank())
                        .coupon(winner.getEvent())
                        .build())
                .collect(Collectors.toList());
    }

    public void validRequest(PostEventRequestDTO userId) {
        // 1. 해당 유저가 이미 참여했는지 확인
        if (attemptRepository.existsByAttemptAndEvent(userId)) {
            throw new IllegalArgumentException("이미 참여한 유저입니다.");
        }
    }
}
