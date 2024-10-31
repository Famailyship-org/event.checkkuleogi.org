package com.system.fcfs.event.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.repository.WinnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;


@Log4j2
@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final WinnerRepository winnerRepository;

    @Value("${spring.cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void receiveAndSaveMessages() {
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

        for (Message message : messages) {
            Winner winner = parseMessageBody(message);
            if (winner != null) {
                winnerRepository.save(winner);
                deleteMessageFromQueue(message);
            }
        }

    }

    private Winner parseMessageBody(Message message) {
        try {
            // JSON 메시지의 body 파싱
            JsonNode bodyNode = objectMapper.readTree(message.body());

            // JSON에서 필요한 필드를 추출하여 Winner 객체에 매핑
            return Winner.builder()
                    .winner(bodyNode.get("winner").asText())         // winner 필드에 매핑
                    .timeStamp(bodyNode.get("timeStamp").asText())   // timeStamp 필드에 매핑
                    .rank(bodyNode.get("rank").asLong())             // rank 필드에 매핑
                    .event(bodyNode.get("event").asText())           // event 필드에 매핑
                    .build();
        } catch (Exception e) {
            log.info("content: " + e);
            e.printStackTrace();
            return null;
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