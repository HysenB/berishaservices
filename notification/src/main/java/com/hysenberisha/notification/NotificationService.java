package com.hysenberisha.notification;


import com.hysenberisha.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;


    public void send(NotificationRequest notificationRequest){
        notificationRepository.save(
                Notification.builder()
                        .toCustomerId(notificationRequest.toCustomerId())
                        .toCustomerEmail(notificationRequest.toCustomerName())
                        .sender("HysenBerisha")
                        .message(notificationRequest.message())
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}
