package com.system.fcfs.ver2;

import com.system.fcfs.ver2.constant.Event;
import com.system.fcfs.ver2.domain.EventCount;
import com.system.fcfs.ver2.domain.Gifticon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class GifticonService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final long FIRST_ELEMENT = 0;
    private static final long LAST_ELEMENT = -1;
    private static final long PUBLISH_SIZE = 10;
    private static final long LAST_INDEX = 1;
    private EventCount eventCount;

    public void setEventCount(Event event, int queue){
        this.eventCount = new EventCount(event, queue);
    }

    // 대기열에 사용자를 추가하는 메서드
    public void addQueue(Event event) {
        final String people = Thread.currentThread().getName();
        final long now = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(event.toString(), people, (int) now);
        log.info("대기열에 추가 - {} ({}초)", people, now);
    }

    // 현재 대기열에서 순서를 조회하는 메서드
    public void getOrder(Event event) {
        final long start = FIRST_ELEMENT;
        final long end = LAST_ELEMENT;

        // 대기열을 오름차순으로 조회한다. start부터 end까비 범위 내의 사용자들을 가져온다.
        Set<Object> queue = redisTemplate.opsForZSet().range(event.toString(), start, end);

        // 가져온 값(사람들)에 대해서 순위(남은 대기 인원)을 조회
        for(Object people : queue){
            Long rank = redisTemplate.opsForZSet().rank(event.toString(), people);
            log.info("'{}'님의 현재 대기열은 {}명 남았습니다.", people, rank);
        }
    }

    // 기프티콘을 발급하는 메서드
    public void publish(Event event){
        final long start = FIRST_ELEMENT;
        final long end = PUBLISH_SIZE - LAST_INDEX;

        // 현재 대기열을 조회
        Set<Object> queue = redisTemplate.opsForZSet().range(event.toString(), start, end);
        for(Object people : queue){
            // 기프티콘 객체 생성 - 생성 시, UUID를 만들기 위해서
            final Gifticon gifticon = new Gifticon(event);
            log.info("'{}'님의 {} 기프티콘이 발급되었습니다({})", people, gifticon.getEvent().getName(), gifticon.getCode());
            redisTemplate.opsForZSet().remove(event.toString(), people);
            this.eventCount.decrease();
        }
    }

    public boolean validEnd(){
        return this.eventCount != null
                ? this.eventCount.end()
                : false;
    }

    public long getSize(Event event){
        return redisTemplate.opsForZSet().size(event.toString());
    }
}
