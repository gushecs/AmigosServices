package com.amigos.customer;

import com.amigoscode.amqp.RabbitMQMessageProducer;
import com.amigoscode.clients.fraud.FraudCheckResponse;
import com.amigoscode.clients.fraud.FraudClientKube;
import com.amigoscode.clients.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@Profile({"kube"})
public record CustomerServiceKube(CustomerRepository customerRepository,
                                  RestTemplate restTemplate,
                                  FraudClientKube fraudClient,
                                  RabbitMQMessageProducer rabbitMQMessageProducer) {

    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        log.info("Sending notification @CustomerService through rabbitmq");
        NotificationRequest notificationRequest = new NotificationRequest(customer.getId(), customer.getEmail(),
                "Hi "+customer.getFirstName()+" "+customer.getLastName()+", Thank you for registering with our API!",
                "Hello "+customer.getFirstName()+" "+customer.getLastName()+".\n\nThank you for registering with us, it's" +
                        " nice to have you through this learning journey 😊.\n\nThis is a standardized e-mail to let you know we " +
                        "are ok, the system is here up and running! 🥳\n\nYour request entered our kubernetes server, went through " +
                        "3 microservices to be sent to you. Quite a journey, hum?\n\nIf you have any questions, don't hesitate to contact " +
                        "us!\n\nDearly, Gustavo's API.");
        rabbitMQMessageProducer.publish(notificationRequest,
                "internal.exchange", "internal.notification.routing-key");

    }

    public Customer findById(Integer customerId){
        return customerRepository.findById(customerId).orElseThrow();
    }

}


