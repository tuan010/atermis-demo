package com.messagequeue.example.artemis_demo;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueBConsumer {

    @JmsListener(destination = "BBroker::b", containerFactory = "myFactory")
    public void failMessage(Message message) throws JMSException {
        int retried = message.propertyExists("xRetried") ? message.getIntProperty("xRetried") : 0;
        String body = ((TextMessage) message).getText();
        System.out.println("🔥 Failing B message: " + body + " | retry=" + retried);
        throw new RuntimeException("Failing to push to DLQ");
    }

}

