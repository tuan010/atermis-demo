package com.messagequeue.example.artemis_demo;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ArtemisConsumer {

    private final JmsTemplate jmsTemplate;

    public ArtemisConsumer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

//    @JmsListener(destination = "DLQ::dlq1", containerFactory = "myFactory")
//    public void dlq1Listener(Message msg) throws JMSException {
//        String orig = msg.getStringProperty("_AMQ_ORIG_QUEUE");
//        String body = ((TextMessage) msg).getText();
//        System.out.println("✅ DLQ1: " + body + " (from: " + orig + ")");
//    }



    @JmsListener(destination = "TCBroker::queue2", containerFactory = "myFactory")
    public void failQueue1(Message msg) throws JMSException {
        String body = ((TextMessage) msg).getText();
        System.out.println("🔥 FAIL queue2: " + body);
        throw new RuntimeException("fail queue2");
    }

}
