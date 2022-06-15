package com.amigoscode.notification.kafka;

import com.amigoscode.clients.notification.NotificationRequest;
import com.amigoscode.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.client.ConditionalnOnSleuthWebClient;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
@Slf4j
@Profile("kafka")
public class KafkaListeners {

    private final NotificationService notificationService;

    private final Environment environment;

    @KafkaListener(topics = "amigoscode", groupId = "groupId", containerFactory = "factory")
    void listener(NotificationRequest data) {
        String to = "";
        if (environment.getProperty("sender") != null) {
            if (Objects.equals(environment.getProperty("sender"), "db")){
                to = "to notification database.";
            } else if (Objects.equals(environment.getProperty("sender"), "sendgrid")) {
                to = "with SendGrid.";
            }

        }
            log.info("Request {} @ KafkaListeners.listener. Posting {}}", data, to);
            notificationService.notify(data);
            log.info("Request {} @ KafkaListeners.listener. Posted {}}", data, to);
    }

}
