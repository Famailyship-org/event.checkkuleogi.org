package com.system.fcfs.event.repository;

import com.system.fcfs.event.consumer.SqsMessageListener;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import com.system.fcfs.event.producer.SqsMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sqsAttemptRepository")
@RequiredArgsConstructor
public class SqsAttemptRepository implements AttemptRepository {

    private final SqsMessageSender sqsMessageSender;
    private final SqsMessageListener sqsMessageListener;

    @Override
    public List<Winner> getTop100Winners(String eventName) {
        return sqsMessageListener.receiveAndSaveMessages();
    }

    @Override
    public Boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO) {
        return false;
    }

    @Override
    public Boolean addQueue(PostEventRequestDTO postEventRequestDTO) {
        String message = postEventRequestDTO.toString();
        return sqsMessageSender.sendMessage(message) != null;
    }
}
