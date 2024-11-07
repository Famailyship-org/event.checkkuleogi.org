package com.system.fcfs.event.presentation;

import com.system.fcfs.event.dto.request.GetWinnerRequestDTO;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.dto.response.GetWinnerResponseDTO;
import com.system.fcfs.global.domain.response.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import static com.system.fcfs.global.domain.response.CommonResponseEntity.success;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventUseCase eventUseCase;

    @CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST})
    @PostMapping("/attempt")
    public CommonResponseEntity requestEvent(@RequestBody PostEventRequestDTO postEventRequestDTO) {
        return success(eventUseCase.addJobQ(postEventRequestDTO));
    }

    // 스케줄 적용 컨트롤러
    @GetMapping("/winners/{eventName}")
    public CommonResponseEntity<Boolean> processScheduledQueue(@PathVariable String eventName) {
        return success(eventUseCase.consumeJobQ(eventName));
    }

    // 당첨자 조회
    @CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST})
    @PostMapping("/winner")
    public CommonResponseEntity<GetWinnerResponseDTO> getWinner(@RequestBody GetWinnerRequestDTO getWinnerRequestDTO) {
        return success(eventUseCase.getWinner(getWinnerRequestDTO));
    }
}

