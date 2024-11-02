package com.system.fcfs.event.presentation;

import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
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

    @CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST})
    @PostMapping("/attempt")
    public CommonResponseEntity requestEvent(@RequestBody PostEventRequestDTO postEventRequestDTO) {
        // userId는 토큰 값을 활용하여 가져오기
        return success(couponService.addQueue(postEventRequestDTO));
    }

    @GetMapping("/winners/{eventName}")
    public CommonResponseEntity<List<GetWinnerResponseDTO>> processScheduledQueue(@PathVariable String eventName) {
        return success(couponService.processScheduledQueue(eventName));
    }

    @CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST})
    @PostMapping("/winner")
    public CommonResponseEntity<GetWinnerResponseDTO> getWinner(@RequestBody GetWinnerRequestDTO getWinnerRequestDTO) {
        return success(couponService.getWinner(getWinnerRequestDTO));
    }
}

