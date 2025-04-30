package com.thp.magalums.repository;

import com.thp.magalums.entity.Notification;
import com.thp.magalums.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Alteração aqui: "Datatime" para "DateTime"
    List<Notification> findByStatusInAndDateTimeBefore(List<Status> status, LocalDateTime dateTime);
}
