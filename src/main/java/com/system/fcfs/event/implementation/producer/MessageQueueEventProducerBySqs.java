package com.system.fcfs.event.implementation.producer;

import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.service.EventProducer;
import com.system.fcfs.global.domain.exception.NotFoundException;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Log4j2
@Component("messageQueueEventProducerBySqs")
public class MessageQueueEventProducerBySqs implements EventProducer {
    private final SqsTemplate queueMessagingTemplate;
    private final NoSqlEventProducerByRedis noSqlEventProducerByRedis;

    @Value("${spring.cloud.aws.sqs.queue-name}")
    private String QUEUE_NAME;

    public MessageQueueEventProducerBySqs(SqsAsyncClient sqsAsyncClient, NoSqlEventProducerByRedis noSqlEventProducerByRedis) {
        this.queueMessagingTemplate = SqsTemplate.newTemplate(sqsAsyncClient);
        this.noSqlEventProducerByRedis = noSqlEventProducerByRedis;
    }

    @Override
    public Boolean validRequest(PostEventRequestDTO postEventRequestDTO) {
        return null;
    }

    @Override
    public Boolean addJobQ(PostEventRequestDTO postEventRequestDTO) {
        if (noSqlEventProducerByRedis.validRequest(postEventRequestDTO)) {
            throw new NotFoundException("중복 응모입니다.");
        }

        postEventRequestDTO.setTimeStamp(java.time.LocalDateTime.now().toString());
        String message = postEventRequestDTO.toString();
        log.info("Send message to SQS: {}", postEventRequestDTO.toString());
        return sendMessage(message) != null;
    }

    public SendResult<String> sendMessage(String message) {
        return queueMessagingTemplate.send(to -> to
                .queue(QUEUE_NAME)
                .payload(message));
    }
}