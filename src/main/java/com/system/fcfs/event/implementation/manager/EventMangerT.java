package com.system.fcfs.event.implementation.manager;

import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.producer.SqsMessageSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@AllArgsConstructor
public class EventMangerT {
    private final SqsMessageSender sqsMessageSender;

    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageDeduplicationId(deduplicationId)
                .messageGroupId(messageGroupId)
                .build();
        sqsMessageSender.sendMessage(postEventRequestDTO.toString());
        return true;
    }
}
