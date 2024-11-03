package com.system.fcfs.event.consumer;

import com.system.fcfs.event.domain.Event;
import com.system.fcfs.event.producer.AttemptProducerByRedis;
import com.system.fcfs.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisZsetReader {

    private final AttemptProducerByRedis attemptProducerByRedis;
    private final EventRepository eventRepository;

    @Scheduled(cron = "00 50 12 * * ?")
    @Transactional
    public void consume() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Optional<Event> previousDayEvent = eventRepository.findPreviousDayEvent(yesterday);

        previousDayEvent.ifPresent(event -> {
            String eventName = event.getEventType().getName();
            attemptProducerByRedis.getTop100AndUpdateQueue(eventName);
        });
    }

}
