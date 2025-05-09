package com.thp.magalums.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long notificationId;

    @Column(name = "dateTime")
    private LocalDateTime dateTime; // Corrigido para "dateTime" (erro de digitação)

    private String destination;

    private String message;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public Notification() {
    }

    public Notification(LocalDateTime dateTime, String destination, String message, Channel channel, Status status) {
        this.dateTime = dateTime; // Usando "dateTime" aqui também
        this.destination = destination;
        this.message = message;
        this.channel = channel;
        this.status = status;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public LocalDateTime getDateTime() { // Corrigido para "getDateTime"
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) { // Corrigido para "setDateTime"
        this.dateTime = dateTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
