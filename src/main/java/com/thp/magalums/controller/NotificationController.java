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
    public ResponseEntity<Void> scheduleNotification(@RequestBody ScheduleNotificationDto dto) {
        try {
            // Verifica e cria a notificação
            notificationService.scheduleNotification(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();  // 201 Created, sem corpo
        } catch (IllegalArgumentException e) {
            // Se a data for inválida, retorna erro com 400 Bad Request e a mensagem no corpo
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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
        // Tenta atualizar a notificação
        boolean updated = notificationService.updateNotification(notificationId, dto);

        // Se a notificação foi atualizada com sucesso
        if (updated) {
            return ResponseEntity.ok().build();  // Atualização bem-sucedida
        }

        // Recupera a notificação para verificarmos o status
        var notificationOpt = notificationService.findById(notificationId);
        if (notificationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A notificação com o ID " + notificationId + " não foi encontrada.");
        }

        var notification = notificationOpt.get();

        // Se o status não for PENDING (statusId != 1), a atualização não é permitida
        if (notification.getStatus().getStatusId() != 1) {  // 1 é o status "PENDING"
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Não é possível atualizar a notificação porque o status atual não é 'PENDING'.");
        }

        // Se o status for PENDING, valida o campo dateTime
        if (dto.dateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O campo 'dateTime' não pode ser no passado.");
        }

        // Caso o status seja 'PENDING' e o dateTime for válido, a notificação será atualizada
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro inesperado ao tentar atualizar a notificação.");
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> cancelNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.cancelNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}

