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

    public void scheduleNotification(ScheduleNotificationDto dto) {
        // Verifica se a data é no passado
        if (dto.dateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data e hora não podem ser no passado.");
        }

        // Se a data for válida, cria a notificação
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

    public boolean updateNotification(Long notificationId, ScheduleNotificationDto dto) {
        var notificationOpt = findById(notificationId);
        if (notificationOpt.isEmpty()) {
            return false; // Notificação não encontrada
        }

        var notification = notificationOpt.get();

        // Verifica se o status_id é 1 (PENDING) antes de permitir a atualização
        if (notification.getStatus().getStatusId() != 1) {
            return false; // Não pode atualizar notificações que não estão "PENDING"
        }

        // Verifica se o novo dateTime não é no passado
        if (dto.dateTime().isBefore(LocalDateTime.now())) {
            return false; // Não pode atualizar a notificação com um dateTime no passado
        }

        // Atualiza os dados da notificação
        notification.setDateTime(dto.dateTime());
        notification.setDestination(dto.destination());
        notification.setMessage(dto.message());
        notification.setChannel(dto.channel().toChannel());

        notificationRepository.save(notification);
        return true;
    }

    public void checkAndSend(LocalDateTime datetime) {
        var notifications = notificationRepository.findByStatusInAndDateTimeBefore(
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
