package com.messagequeue.example.artemis_demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProducerController {

    private final ArtemisProducer producer;

    public ProducerController(ArtemisProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/send")
    public String send(@RequestParam String queue, @RequestParam String message) {
        producer.sendToQueue(queue, message);
        return "Message sent to " + queue;
    }
}
