package com.system.fcfs.event.component;

import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Component
public class SqsMessageSender {
    private final SqsTemplate queueMessagingTemplate;

    @Value("${spring.cloud.aws.sqs.queue-name}")
    private String QUEUE_NAME;

    public SqsMessageSender(SqsAsyncClient sqsAsyncClient) {
        this.queueMessagingTemplate = SqsTemplate.newTemplate(sqsAsyncClient);
    }

    public SendResult<String> sendMessage(String message) {
        return queueMessagingTemplate.send(to -> to
                .queue(QUEUE_NAME)
                .payload(message));
    }
}