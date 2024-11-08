package com.system.fcfs.event.implementation.consumer;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.implementation.manager.EventManger;
import com.system.fcfs.event.repository.AttemptRepository;
import com.system.fcfs.event.repository.WinnerRepository;
import com.system.fcfs.event.service.EventConsumer;
import com.system.fcfs.global.domain.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("rdbEventConsumerByJpa")
@AllArgsConstructor
@Log4j2
public class RdbEventConsumerByJpa implements EventConsumer {
    private final AttemptRepository attemptRepository;
    private final WinnerRepository winnerRepository;
    private final EventManger eventManger;

    @Override
    public Boolean consumeJobQ(String eventName) {
        Pageable pageable = PageRequest.of(0, 100);
        List<Attempt> attempts = attemptRepository.findTop100(pageable).orElseThrow(() -> new NotFoundException("당첨자가 없습니다."));
        List<Winner> winners = eventManger.attemptsToWinners(attempts);
        if (!winnerRepository.findAll().isEmpty()) {
            throw new RuntimeException("이미 당첨자가 존재합니다.");
        }
        winnerRepository.saveAll(winners);
        return true;
    }


    @Override
    public Winner getWinner(GetWinnerRequestDTO getWinnerRequestDTO) {
        return winnerRepository.findByUserNameAndPhoneNum(getWinnerRequestDTO.userName(), getWinnerRequestDTO.phoneNum()).orElseThrow(
                () -> new NotFoundException("당첨자가 없습니다."));
    }
}
