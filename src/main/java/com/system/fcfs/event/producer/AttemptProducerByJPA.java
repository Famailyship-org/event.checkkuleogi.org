package com.system.fcfs.event.producer;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.repository.AttemptJpaRepository;
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
        return attemptJpaRepository.findTop100(pageable);
    }

    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        return attemptJpaRepository.existsByUserId(postEventRequestDTO.userId());
    }

    @Override
    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        attemptJpaRepository.save(Attempt.builder()
                .userName(postEventRequestDTO.userId())
                .timeStamp(postEventRequestDTO.timestamp())
                .phoneNum(postEventRequestDTO.phoneNum())
                .build());
        return true;
    }
}
