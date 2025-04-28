package com.messagequeue.example.artemis_demo;

import jakarta.jms.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class ArtemisConsumer {

    private final JmsTemplate jmsTemplate;

    public ArtemisConsumer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


//    @JmsListener(destination = "ABroker::a", containerFactory = "myFactory")
//    public void failQueueA(Message msg) throws JMSException {
//        String body = ((TextMessage) msg).getText();
//        System.out.println("🔥 FAIL a: " + body);
//
//        // simulate fail to trigger DLQ
//        throw new RuntimeException("Force retry after 1 min");
//    }



//    @JmsListener(destination = "BBroker::b", containerFactory = "myFactory")
//    public void failQueueB(Message msg) throws JMSException {
//        String body = ((TextMessage) msg).getText();
//        System.out.println("🔥 FAIL b: " + body);
//        throw new RuntimeException("fail b");
//    }

//    @JmsListener(destination = "CBroker::c", containerFactory = "myFactory")
//    public void failQueueC(Message msg) throws JMSException {
//        String body = ((TextMessage) msg).getText();
//        System.out.println("🔥 FAIL c: " + body);
//        throw new RuntimeException("fail c");
//    }

    @JmsListener(destination = "DLQ1::dlq1", containerFactory = "myFactory")
    public void retryFromDLQ1(TextMessage msg) throws JMSException {
        int retries = msg.propertyExists("x_retried") ? msg.getIntProperty("x_retried") : 0;

        if (retries >= 3) {
            System.out.println("🛑 No more retries for: " + msg.getText());
            return;
        }

        // Re-send to origin with incremented retry count
        Connection connection = jmsTemplate.getConnectionFactory().createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        TextMessage newMsg = session.createTextMessage(msg.getText());

        newMsg.setIntProperty("x_retried", retries + 1);
        jmsTemplate.convertAndSend("ABroker::a", newMsg);

        System.out.println("🔁 Retried to A with x_retried=" + (retries + 1));

        session.close();
        connection.close(); // important to close
    }


//    @JmsListener(destination = "DLQ2::dlq2", containerFactory = "myFactory")
//    public void retryDLQ(Message message) throws JMSException {
//        int retried = message.propertyExists("xRetried") ? message.getIntProperty("xRetried") : 0;
//        String body = ((TextMessage) message).getText();
//
//        if (retried >= 3) {
//            System.out.println("🚫 Abandon message: " + body);
//            // Send to final abandoned DLQ
//            jmsTemplate.send("DLQ2::dlq2.abandoned", session -> {
//                TextMessage abandonedMsg = session.createTextMessage(body);
//                abandonedMsg.setIntProperty("xRetried", retried);
//                return abandonedMsg;
//            });
//            return;
//        }
//
//        System.out.println("🔁 Retrying: " + body + " (attempt: " + (retried + 1) + ")");
//
//        try {
//            Thread.sleep(3000); // give time to see in console
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        // send back with incremented retry count
//        jmsTemplate.send("BBroker::b", session -> {
//            TextMessage newMsg = session.createTextMessage(body);
//            newMsg.setIntProperty("xRetried", retried + 1); // 👈 retry count tracked by YOU
//            return newMsg;
//        });
//    }


//    @JmsListener(destination = "DLQ3::dlq3", containerFactory = "myFactory")
//    public void retryFromDLQ3(TextMessage msg) throws JMSException {
//        int retries = msg.propertyExists("x-retried") ? msg.getIntProperty("x-retried") : 0;
//
//        if (retries >= 3) {
//            System.out.println("🛑 No more retries for: " + msg.getText());
//            return;
//        }
//
//        // Re-send to origin with incremented retry count
//        Message newMsg = jmsTemplate.getConnectionFactory()
//                .createConnection()
//                .createSession(false, Session.AUTO_ACKNOWLEDGE)
//                .createTextMessage(msg.getText());
//
//        newMsg.setIntProperty("x-retried", retries + 1);
//        jmsTemplate.convertAndSend("CBroker::c", newMsg);
//        System.out.println("🔁 Retried to C with x-retried=" + (retries + 1));
//    }

}
