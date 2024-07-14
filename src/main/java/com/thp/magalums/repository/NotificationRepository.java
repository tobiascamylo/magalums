package com.thp.magalums.repository;

import com.thp.magalums.entity.Notification;
import com.thp.magalums.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatusInAndDatatimeBefore(List<Status> status, LocalDateTime dataTime);
}
