package com.messagequeue.example.artemis_demo;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ArtemisConsumer {

    private final JmsTemplate jmsTemplate;

    public ArtemisConsumer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "DLQ.dlq1", containerFactory = "myFactory")
    public void listenDLQ1(String message) {
        System.out.println("Received from DLQ.dlq1: " + message);
        retry("TCBroker.queue1", message);
    }

    //Test DLQ
//    @JmsListener(destination = "TCBroker", containerFactory = "myFactory")
//    public void listen(String message) throws Exception {
//        System.out.println("Received: " + message);
//
//        // Simulate a failure to trigger DLQ logic
//        throw new RuntimeException("Simulated failure to trigger DLQ");
//    }


    private void retry(String originalQueue, String message) {
        // Retry logic (basic version)
        System.out.println("Retrying message to original queue: " + originalQueue);
        jmsTemplate.convertAndSend(originalQueue, message);
    }
}
