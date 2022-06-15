package com.amigoscode;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "amigoscode", groupId = "groupId", containerFactory = "factory")
    void listener(Message data) {
        System.out.println("Listener received: "+ data + " ✨");
    }

}
