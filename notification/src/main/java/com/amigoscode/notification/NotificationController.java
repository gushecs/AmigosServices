package com.amigoscode.notification;

import com.amigoscode.clients.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/notification")
public record NotificationController (NotificationService notificationService) {

    @PostMapping
    public void notify(@RequestBody NotificationRequest notificationRequest) {
        log.info("notify call for customer {}", notificationRequest.toCustomerId());
        notificationService.notify(notificationRequest);
    }

}
