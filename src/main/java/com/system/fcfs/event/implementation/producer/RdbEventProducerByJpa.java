package com.system.fcfs.event.implementation.producer;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.repository.JpaEventRepository;
import com.system.fcfs.event.service.EventProducer;
import com.system.fcfs.global.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component("rdbEventProducerByJpa")
@RequiredArgsConstructor
public class RdbEventProducerByJpa implements EventProducer {

    private final JpaEventRepository jpaEventRepository;

    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        return jpaEventRepository.existsByUserId(postEventRequestDTO.getUserName());
    }

    @Override
    public Boolean addJobQ(PostEventRequestDTO postEventRequestDTO) {
        if(validRequest(postEventRequestDTO)){
            throw new NotFoundException("중복 응모입니다.");
        }
        String time = java.time.LocalDateTime.now().toString();
        jpaEventRepository.save(Attempt.builder()
                .userName(postEventRequestDTO.getUserName())
                .timeStamp(time)
                .phoneNum(postEventRequestDTO.getPhoneNum())
                .build());
        return true;
    }
}
