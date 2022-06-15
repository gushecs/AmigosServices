package com.amigoscode.notification;

import com.amigoscode.clients.notification.NotificationRequest;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public record NotificationService(NotificationRepository notificationRepository, Environment environment) {

    public void notify(NotificationRequest notificationRequest) {
        String sender = environment.getProperty("sender");
        if (sender != null && sender.equals("db")) {
            log.info("Saving notification to customer {} to DB",notificationRequest.toCustomerId());
            notificationRepository.save(new Notification(null, notificationRequest.toCustomerId(),
                    notificationRequest.toCustomerEmail(),
                    "customer api",
                    notificationRequest.title(),
                    notificationRequest.message(),
                    LocalDateTime.now()));
        } else if (sender != null && sender.equals("sendgrid")) {
            log.info("Sending notification to customer {} with SendGrid",notificationRequest.toCustomerId());
            Email from = new Email(System.getenv("MY_EMAIL_GMAIL"));
            String subject = notificationRequest.title();
            Email to = new Email(notificationRequest.toCustomerEmail());
            Content content = new Content("text/plain", notificationRequest.message());
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
