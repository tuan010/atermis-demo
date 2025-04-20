package com.messagequeue.example.artemis_demo;

import jakarta.annotation.PostConstruct;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestMessageSender {
    private final JmsTemplate jmsTemplate;

    public TestMessageSender (JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

//    @PostConstruct
//    public void sendTestMessage() {
//        jmsTemplate.convertAndSend("TCBroker::queue1", "✅ test queue1");
//    }

}
