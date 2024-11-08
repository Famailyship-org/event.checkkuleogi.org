package com.system.fcfs.event.implementation.manager;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EventManger {
    public static List<Winner> attemptsToWinners(List<Attempt> attempts) {
        List<Winner> winners = attempts.stream().map(attempt -> {
            String uuid = UUID.randomUUID().toString();  // 고유한 UUID 생성
            return Winner.builder()
                    .userName(attempt.getUserName())
                    .phoneNum(attempt.getPhoneNum())
                    .eventName(attempt.getEventName())
                    .timeStamp(attempt.getTimeStamp())
                    .uuid(uuid).build();
        }).collect(Collectors.toList());
        return winners;
    }
}
