package com.amigos.customer;

import com.amigoscode.clients.fraud.FraudCheckResponse;
import com.amigoscode.clients.fraud.FraudClient;
import com.amigoscode.clients.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@Slf4j
@Profile("kafka")
public record CustomerServiceKafka(CustomerRepository customerRepository,
                                   RestTemplate restTemplate,
                                   FraudClient fraudClient,
                                   KafkaTemplate<String, NotificationRequest> kafkaTemplate) {

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

        log.info("Sending notification @CustomerService through kafka");
        NotificationRequest notificationRequest = new NotificationRequest(customer.getId(), customer.getEmail(),
                "Hi "+customer.getFirstName()+" "+customer.getLastName()+", Thank you for registering with our API!",
                "Hello "+customer.getFirstName()+" "+customer.getLastName()+".\n\nThank you for registering with us, it's" +
                        " nice to have you through this learning journey 😊.\n\nThis is a standardized e-mail to let you know we " +
                        "are ok, the system is here up and running! 🥳\n\nYour request arrived in our customer registration microservice" +
                        ", went through the fraud-check and notification, where it was sent to you. Quite a journey, hum?" +
                        "\n\nIf you have any questions, don't hesitate to contact " +
                        "us!\n\nDearly, Gustavo's API.");
        kafkaTemplate.send("amigoscode", notificationRequest);

    }

    public Customer findById(Integer customerId){
        return customerRepository.findById(customerId).orElseThrow();
    }

}


