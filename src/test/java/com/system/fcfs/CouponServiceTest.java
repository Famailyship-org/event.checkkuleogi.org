package com.system.fcfs;

import com.system.fcfs.infra.CouponRepository;
import com.system.fcfs.service.CouponService;
import com.system.fcfs.ver2.GifticonService;
import com.system.fcfs.ver2.constant.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;



    @Test
    void 한번만응모() {
        // Given: 쿠폰을 처음 응모하는 상황
        Long userId = 1L;

        // When: 해당 유저가 쿠폰에 응모
        couponService.apply(userId);

        // Then: 쿠폰이 정확히 한 번만 응모되었는지 확인
        long count = couponRepository.count();
        System.out.println("count = " + count);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    void 여러번응모() throws InterruptedException{
        int threadCount = 1000;

        // 32개의 쓰레드풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 1000개의 작업이 끝나야 다음으로 진행할 수 있도록 하는 장치의 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        /*
        * 1. 반복문 내의 executorsService.submit()는비동기적으로 로직을 수행한다
        * 2. 메인 쓰레드는 반복문에서 내려와 로직을 끝나게 된다
        * 3. latch.await()이 있으므로 메인 쓰레드는 이 모든 latch들의 작업이 끝날 때까지 대기상태
        * 4. 이후 count가 0에 다다르면 메인 쓰레드를 대기상태로부터 깨워 남은 로직을 수행하게 한다.*/

        for(int i = 1; i<= 1000; i++){
            long memberId = i;
            executorService.submit(() ->{
                try{
                    couponService.apply(memberId);
                }finally {
                    // 1000부터 줄여나가서 0이 되면 메인 쓰레드를 대기상태에서 해제한다.
                    latch.countDown();
                }
            });
        }

        latch.await();
        long count = couponRepository.count();
        System.out.println("count = " + count);
        assertThat(count).isEqualTo(100);

    }


}
