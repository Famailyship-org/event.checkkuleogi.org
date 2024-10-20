package com.system.fcfs.ver2;

import com.system.fcfs.ver2.constant.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final GifticonService gifticonService;

    // 1초마다 도는 대기열 동기화 스케줄러
    @Scheduled(fixedDelay = 1000)
    private void chickenEventScheduler(){
        if(gifticonService.validEnd()){
            log.info("==== 선착순 이벤트가 종료되었습니다. ====");
            return;
        }
        // 종료되지 않았으면 기프티콘을 발급하고 남은 대기열에 순번 표출
        gifticonService.publish(Event.CHICKEN);
        gifticonService.getOrder(Event.CHICKEN);
    }

}
