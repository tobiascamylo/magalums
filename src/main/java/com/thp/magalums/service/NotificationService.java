package com.thp.magalums.service;

import com.thp.magalums.controller.dto.ScheduleNotificationDto;
import com.thp.magalums.entity.Notification;
import com.thp.magalums.entity.Status;
import com.thp.magalums.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public  void scheduleNotification(ScheduleNotificationDto dto) {
        notificationRepository.save(dto.toNotification());
    }

    public Optional<Notification> findById(Long notificationId) {
        return notificationRepository.findById(notificationId);
    }

    public void cancelNotification(Long notificationId) {
        var notification = findById(notificationId);
        if (notification.isPresent()){
            notification.get().setStatus(Status.Values.CANCELED.toStatus());
            notificationRepository.save(notification.get());
        }
    }

    public void checkAndSend(LocalDateTime datetime) {
        var notifications = notificationRepository.findByStatusInAndDatatimeBefore(
                List.of(Status.Values.PENDING.toStatus(), Status.Values.ERROR.toStatus()),
                datetime
        );

        notifications.forEach(sendNotification());
    }

    private Consumer<Notification> sendNotification() {
        return n -> {

            // TODO - REALIZAR O ENVIO DA NOTIFICACAO

            n.setStatus(Status.Values.SUCESS.toStatus());
            notificationRepository.save(n);
        };
    }
}
