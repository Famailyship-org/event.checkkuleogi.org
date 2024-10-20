package com.system.fcfs;

import com.system.fcfs.ver2.GifticonService;
import com.system.fcfs.ver2.constant.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventServiceTest {
    @Autowired
    private GifticonService gifticonService;
    @Test
    void 선착순이벤트_100명에게_기프티콘_30개_제공() throws InterruptedException{
        final Event chickenEvent = Event.CHICKEN;
        final int people = 100;
        final int limitCount = 30;

        // 해당 파라미터마늠으 ㅣ쓰레드가 작업을 완료할 때까지 기다리도록 설정된다.
        // 1. 쓰레드 시작 시점 동기화
        // 2. 작업 완료 기다림
        // 3. 단일 쓰레드 작업 완료 대기
        final CountDownLatch countDownLatch = new CountDownLatch(people);
        gifticonService.setEventCount(chickenEvent, limitCount);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker(countDownLatch, chickenEvent)))
                .limit(people)
                .collect(Collectors.toList());
        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(5000);

        final long failEventPeople = gifticonService.getSize(chickenEvent);
        assertEquals(people - limitCount, failEventPeople);
    }

    private class AddQueueWorker implements Runnable{
        private CountDownLatch countDownLatch;
        private Event event;

        public AddQueueWorker(CountDownLatch countDownLatch, Event event){
            this.countDownLatch = countDownLatch;
            this.event = event;
        }

        @Override
        public void run() {
            gifticonService.addQueue(event);
            countDownLatch.countDown();
        }
    }
}
