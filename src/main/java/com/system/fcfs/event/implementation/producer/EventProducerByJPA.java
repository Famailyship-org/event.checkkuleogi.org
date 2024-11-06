package com.system.fcfs.event.implementation.producer;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.repository.JpaEventRepository;
import com.system.fcfs.event.service.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository("jpaWinnerRepository")
@RequiredArgsConstructor
public class EventProducerByJPA implements EventProducer {

    private final JpaEventRepository jpaEventRepository;

    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        return jpaEventRepository.existsByUserId(postEventRequestDTO.getUserName());
    }

    @Override
    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        String time = java.time.LocalDateTime.now().toString();
        jpaEventRepository.save(Attempt.builder()
                .userName(postEventRequestDTO.getUserName())
                .timeStamp(time)
                .phoneNum(postEventRequestDTO.getPhoneNum())
                .build());
        return true;
    }
}
