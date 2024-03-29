package com.app.messagingapplication.service;

import com.app.messagingapplication.entity.UserMessage;
import com.app.messagingapplication.repository.UserMessageRepository;
import com.app.messagingapplication.utility.custom_exceptions.InvalidDataException;

import java.util.List;

import com.app.messagingapplication.utility.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserMessageService implements IUserMessageService {

    @Autowired
    UserMessageRepository userMessageRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    public String sendMessage(UserMessage userMessage) throws InvalidDataException {
        try {
            boolean isValid = validateMessageDetails(userMessage);

            if(isValid){
                log.info("Sending message to {}", userMessage.getReceiver());
                UserMessage saveUserMessageResult = userMessageRepository.save(userMessage);
                //Put message on Kafka queue
                kafkaProducer.sendMessage(saveUserMessageResult);
                return "Message sent to " + saveUserMessageResult.getReceiver();
            }

            throw new InvalidDataException("You cannot send a message to yourself");
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new InvalidDataException("Invalid sender/receiver ID");
        }
    }

    public List<UserMessage> viewSentMessages(String sender) {
        try {
            log.info("Fetching messages sent from ID {} ", sender);
            return userMessageRepository.findAllBySender(sender);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException(runtimeException.getMessage());
        }
    }

    public List<UserMessage> viewReceivedMessages(String receiver) {
        try {
            log.info("Fetching received messages for ID {} ", receiver);
            return userMessageRepository.findAllByReceiver(receiver);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException(runtimeException.getMessage());
        }
    }

    public List<UserMessage> viewMessagesFromUser(String sender, String receiver) {
        try {
            log.info("Fetching messages sent from {} to {} ", sender, receiver);
            return userMessageRepository.findAllBySenderAndReceiver(sender, receiver);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException(runtimeException.getMessage());
        }
    }

    private boolean validateMessageDetails(UserMessage userMessage){
        return !userMessage.getReceiver().equalsIgnoreCase(userMessage.getSender());
    }

}
