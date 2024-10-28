package com.system.fcfs.event.controller;

import com.system.fcfs.event.constant.Event;
import com.system.fcfs.event.dto.GetWinnerResponseDTO;
import com.system.fcfs.event.dto.PostEventRequestDTO;
import com.system.fcfs.event.scheduler.EventScheduler;
import com.system.fcfs.event.service.EventService;
import com.system.fcfs.global.domain.response.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.system.fcfs.global.domain.response.CommonResponseEntity.success;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService couponService;

    @PostMapping("/attempt")
    public CommonResponseEntity requestEvent(@RequestBody PostEventRequestDTO postEventRequestDTO) {
        log.info("Received request: {}", postEventRequestDTO);
        // 1. 먼저 해당 요청이 유효한지 아닌지 확인한다.
        couponService.validRequest(postEventRequestDTO);
        // 2. 해당 요청을 큐에 넣는다.
        couponService.addQueue(postEventRequestDTO);
        return success(true);
    }

    @GetMapping("/winner")
    public CommonResponseEntity<List<GetWinnerResponseDTO>> getWinner() {
        return success(couponService.getTop100Winners());
    }
}

