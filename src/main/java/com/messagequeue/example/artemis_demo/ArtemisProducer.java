package com.messagequeue.example.artemis_demo;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ArtemisProducer {

    private final JmsTemplate jmsTemplate;

    public ArtemisProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendToQueue1(String message) {
        jmsTemplate.setPubSubDomain(false); // Make sure we are in queue mode
        jmsTemplate.convertAndSend("TCBroker", message, m -> {
            m.setStringProperty("_AMQ_ROUTING_TYPE", "anycast");
            return m;
        });
        System.out.println("Sent to TCBroker.queue1: " + message);
    }

    public void sendToQueue(String queueName, String message) {
        jmsTemplate.convertAndSend(queueName, message);
        System.out.println("Sent to " + queueName + ": " + message);
    }
}
