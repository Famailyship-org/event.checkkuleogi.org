package com.system.fcfs.event.implementation.consumer;

import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.repository.JpaEventRepository;
import com.system.fcfs.event.repository.WinnerRepository;
import com.system.fcfs.event.service.EventConsumer;
import com.system.fcfs.global.domain.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("rdbEventConsumerByJPA")
@AllArgsConstructor
public class RdbEventConsumerByJpa implements EventConsumer {
    private final JpaEventRepository jpaEventRepository;
    private final WinnerRepository winnerRepository;

    @Override
    public Boolean consumeJobQ(String eventName) {
        Pageable pageable = PageRequest.of(0, 100);
        List<Winner> winners = jpaEventRepository.findTop100(pageable).orElseThrow(() -> new NotFoundException("당첨자가 없습니다."));
        winnerRepository.saveAll(winners);
        return true;
    }

    @Override
    public Winner getWinner(GetWinnerRequestDTO getWinnerRequestDTO) {
        return winnerRepository.findByUserNameAndPhoneNum(getWinnerRequestDTO.userName(), getWinnerRequestDTO.phoneNum()).orElseThrow(
                () -> new NotFoundException("당첨자가 없습니다."));
    }
}
