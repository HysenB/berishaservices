package com.hysenberisha.customer;

import com.hysenberisha.clients.fraud.FraudCheckResponse;
import com.hysenberisha.clients.fraud.FraudClient;
import com.hysenberisha.clients.notification.NotificationClient;
import com.hysenberisha.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
//    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        //  todo: check if email valid
        //  todo: check if email not taken
        //  todo: check if fraudster

        // we are doing saveAndFlush so that we can have access to customer ID
        // because if we dont say saveAndFlush and we say only save the customer ID will be null
        customerRepository.saveAndFlush(customer);
//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                "http://FRAUD/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId()
//        );
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("Fraudster");
        }
        //  todo: make it asnyc i.e add to queue
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Hysenberisha...",
                                customer.getFirstName())
                )
        );

    }
}
