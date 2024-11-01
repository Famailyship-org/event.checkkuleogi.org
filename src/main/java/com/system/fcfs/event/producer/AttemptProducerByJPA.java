package com.system.fcfs.event.producer;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.repository.AttemptJpaRepository;
import com.system.fcfs.global.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jpaWinnerRepository")
@RequiredArgsConstructor
public class AttemptProducerByJPA implements AttemptProducer {

    private final AttemptJpaRepository attemptJpaRepository;

    @Override
    public List<Winner> getTop100AndUpdateQueue(String eventName) {
        Pageable pageable = PageRequest.of(0, 100);
        return attemptJpaRepository.findTop100(pageable).orElseThrow(() -> new NotFoundException("당첨자가 없습니다."));
    }

    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        return attemptJpaRepository.existsByUserId(postEventRequestDTO.getUserName());
    }

    @Override
    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        String time = java.time.LocalDateTime.now().toString();
        attemptJpaRepository.save(Attempt.builder()
                .userName(postEventRequestDTO.getUserName())
                .timeStamp(time)
                .phoneNum(postEventRequestDTO.getPhoneNum())
                .build());
        return true;
    }
}
