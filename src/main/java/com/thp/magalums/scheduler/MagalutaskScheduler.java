package com.thp.magalums.scheduler;

import com.thp.magalums.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class MagalutaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MagalutaskScheduler.class);

    public MagalutaskScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private final NotificationService notificationService;


    @Scheduled (fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkTasks() {
        var dateTime = LocalDateTime.now();
        logger.info("Running MagalutaskSchedulerNotification {}",dateTime);
        notificationService.checkAndSend(dateTime);
    }
}


