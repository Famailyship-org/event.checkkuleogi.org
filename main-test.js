import http from 'k6/http';
import { check } from 'k6';

export let options = {
    scenarios: {
        moderate_load_test: {
            executor: 'constant-arrival-rate',
            rate: 3000, // 초당 1,667 요청
            timeUnit: '1s', // rate는 1초를 기준으로 설정
            duration: '1m', // 10분 동안 지속
            preAllocatedVUs: 2000, // 트래픽을 처리할 수 있는 초기 가상 유저 수
            maxVUs: 20000, // 필요시 추가로 할당할 최대 가상 유저 수
        },
    },
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<2000'],
    },
};

export default function () {
    const url = 'http://localhost:8080/event/attempt';
    const randomDigits = () => Math.floor(Math.random() * 9000 + 1000); // 1000-9999 사이의 숫자 생성

    // 고유한 사용자 ID 생성
    const userId = `user-${__VU}-${__ITER}`;
    const eventName = 'FREE_CAMPING';
    const phoneNum = `010${randomDigits()}${randomDigits()}`;

    const payload = JSON.stringify({
        userName: userId,
        eventName: eventName,
        phoneNum: phoneNum,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let res = http.post(url, payload, params);

    check(res, {
        'status was 200': (r) => r.status === 200,
    });

    if (res.status !== 200) {
        console.error(`Request failed. Status: ${res.status}`);
        try {
            const responseBody = JSON.parse(res.body);
            if (responseBody.error) {
                console.error(`Error message: ${responseBody.error.message}`);
            }
        } catch (e) {
            console.error('Failed to parse response body:', e);
        }
    }
}