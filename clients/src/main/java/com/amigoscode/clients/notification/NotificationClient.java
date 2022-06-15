package com.amigoscode.clients.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("NOTIFICATION")
public interface NotificationClient {
    @PostMapping(path = "api/v1/notification")
    public void notify(@RequestBody NotificationRequest notificationRequest);
}