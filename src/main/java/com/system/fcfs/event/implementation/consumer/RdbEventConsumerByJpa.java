package com.system.fcfs.event.implementation.consumer;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.repository.JpaEventRepository;
import com.system.fcfs.event.service.RdbEventConsumer;
import com.system.fcfs.global.domain.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("rdbEventConsumerByJPA")
@AllArgsConstructor
public class RdbEventConsumerByJpa implements RdbEventConsumer {
    private final JpaEventRepository jpaEventRepository;

    @Override
    public List<Winner> getTop100AndUpdateQueue(String eventName) {
        Pageable pageable = PageRequest.of(0, 100);
        return jpaEventRepository.findTop100(pageable).orElseThrow(() -> new NotFoundException("당첨자가 없습니다."));
    }


    @Override
    public Winner getWinner(GetWinnerRequestDTO getWinnerRequestDTO) {
        return null;
    }
}
