package com.system.fcfs.event.repository;

import com.system.fcfs.event.component.SqsMessageListener;
import com.system.fcfs.event.component.SqsMessageSender;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sqsAttemptRepository")
@RequiredArgsConstructor
public class SqsAttemptRepository implements AttemptRepository {

    private final SqsMessageSender sqsMessageSender;  // Add SqsMessageSender as a dependency
    private final SqsMessageListener sqsMessageListener;  // Add SqsMessageSender as a dependency

    @Override
    public List<Winner> geTop100Attempt(Pageable pageable, String eventName) {
        sqsMessageListener.receiveAndSaveMessages();
        return null;
    }

    @Override
    public boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO) {
        throw new UnsupportedOperationException("Not implemented for SQS");
    }

    @Override
    public void addQueue(PostEventRequestDTO postEventRequestDTO) {
        String message = convertPostEventRequestDTOToString(postEventRequestDTO);
        sqsMessageSender.sendMessage(message);  // Send the message to SQS
    }

    private String convertPostEventRequestDTOToString(PostEventRequestDTO postEventRequestDTO) {
        // Implement a method to convert postEventRequestDTO to JSON or another suitable format
        return postEventRequestDTO.toString(); // Replace with actual conversion logic if needed
    }
}
