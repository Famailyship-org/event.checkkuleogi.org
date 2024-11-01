package com.system.fcfs.event.presentation;

import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
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
        // userId는 토큰 값을 활용하여 가져오기
        return success(couponService.addQueue(postEventRequestDTO));
    }

    @GetMapping("/winner/{eventName}")
    public CommonResponseEntity<List<GetWinnerResponseDTO>> processScheduledQueue(@PathVariable String eventName) {
        return success(couponService.processScheduledQueue(eventName));
    }

//    @GetMapping("/winner/{eventName}")
//    public CommonResponseEntity<List<GetWinnerResponseDTO>> getWinner() {
//        return success(couponService.processScheduledQueue(eventName));
//    }
}

