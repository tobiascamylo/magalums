package com.thp.magalums.controller;

import com.thp.magalums.controller.dto.ScheduleNotificationDto;
import com.thp.magalums.entity.Notification;
import com.thp.magalums.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> scheduleNotification(@RequestBody ScheduleNotificationDto dto ) {
       notificationService.scheduleNotification(dto);
       return ResponseEntity.accepted().build();
    }

    @GetMapping("/{notificationId}")
    public  ResponseEntity<Notification> getNotificationById(@PathVariable("notificationId") Long notificationId) {
        var notification = notificationService.findById(notificationId);
        if (notification.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notification.get());
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<String> updateNotification(@PathVariable("notificationId") Long notificationId,
                                                     @RequestBody ScheduleNotificationDto dto) {
        boolean updated = notificationService.updateNotification(notificationId, dto);

        if (updated) {
            return ResponseEntity.ok().build();  // Atualização bem-sucedida
        }

        // Verifica se o erro foi relacionado ao dateTime no passado
        if (dto.dateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O campo 'dateTime' não pode ser no passado.");
        }

        // Se a notificação não foi encontrada
        if (!notificationService.findById(notificationId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A notificação com o ID " + notificationId + " não foi encontrada.");
        }

        // Caso contrário, a notificação não está no status PENDING
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Não é possível atualizar a notificação porque o status atual não é 'PENDING'.");
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> cancelNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.cancelNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}

