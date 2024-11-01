package com.system.fcfs.event.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.repository.AttemptJpaRepository;
import com.system.fcfs.event.repository.EventRepository;
import com.system.fcfs.event.repository.WinnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final WinnerRepository winnerRepository;
    private final EventRepository eventRepository;
    private final AttemptJpaRepository attemptJpaRepository;

    @Value("${spring.cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public List<Winner> receiveAndSaveWinners() {
        List<Winner> winners = new ArrayList<>();
        int totalMessagesToFetch = 100;
        int fetchedMessages = 0;

        while (fetchedMessages < totalMessagesToFetch) {
            int messagesToFetch = Math.min(10, totalMessagesToFetch - fetchedMessages);

            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(messagesToFetch) // 한 번에 가져올 메시지 수 (최대 10개)
                    .waitTimeSeconds(20) // Long Polling 사용
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
            fetchedMessages += messages.size();

            for (Message message : messages) {
                Winner winner = parseMessageBody(message);
                if (winner != null) {
                    winnerRepository.save(winner);
                    deleteMessageFromQueue(message);
                    winners.add(winner);
                }
            }
            // 메시지가 남아있지 않으면 루프 종료
            if (messages.isEmpty()) {
                break;
            }
        }
        return winners;
    }

    public List<Attempt> receiveAndSaveAllAttempts() {
        List<Attempt> attempts = new ArrayList<>();

        while (true) {
            // AWS SQS는 한 번에 최대 10개의 메시지만 가져올 수 있으므로 maxNumberOfMessages를 10으로 설정
            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10) // 한 번에 가져올 최대 메시지 수
                    .waitTimeSeconds(20) // Long Polling 사용
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

            // 메시지를 처리하고 저장
            for (Message message : messages) {
                Attempt attempt = parseAttemptMessageBody(message);
                if (attempt != null) {
                    attemptJpaRepository.save(attempt);
                    deleteMessageFromQueue(message);
                    attempts.add(attempt);
                }
            }

            // 메시지가 남아있지 않으면 루프 종료
            if (messages.isEmpty()) {
                break;
            }
        }
        return attempts;
    }

    private Attempt parseAttemptMessageBody(Message message) {
        try {
            JsonNode bodyNode = objectMapper.readTree(message.body());
            String timeStamp = bodyNode.get("timeStamp").asText();
            String eventName = bodyNode.get("event").asText();
            String phoneNum = bodyNode.get("phoneNum").asText();
            String userName = bodyNode.get("userName").asText();


            return Attempt.builder()
                    .timeStamp(timeStamp)
                    .eventName(eventName)
                    .phoneNum(phoneNum)
                    .userName(userName)
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("메시지 파싱에 실패했습니다. message: " + message.body(), e);
        }
    }

    private Winner parseMessageBody(Message message) {
        try {
            JsonNode bodyNode = objectMapper.readTree(message.body());
            String timeStamp = bodyNode.get("timeStamp").asText();
            String eventName = bodyNode.get("eventName").asText();
            String phoneNum = bodyNode.get("phoneNum").asText();
            String userName = bodyNode.get("userName").asText();

            return Winner.builder()
                    .timeStamp(timeStamp)
                    .userName(userName)
                    .phoneNum(phoneNum)
                    .eventName(eventName)
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("메시지 파싱에 실패했습니다. message: " + message.body(), e);
        }
    }

    private void deleteMessageFromQueue(Message message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle()) // 메시지의 고유한 핸들 값 사용
                .build();
        sqsClient.deleteMessage(deleteRequest);
    }
}